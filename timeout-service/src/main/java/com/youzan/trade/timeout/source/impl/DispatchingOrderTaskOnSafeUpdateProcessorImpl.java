package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.OrderService;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.timeout.source.WhiteShopFilter;
import com.youzan.trade.util.LogUtils;


import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听维权更新，区分维权处于那种状态，来对deliveredOrderTask进行中断/恢复处理
 *
 * @author liwenjia created at: 15/12/31
 */
@Component(value = "dispatchingOrderTaskOnSafeUpdateProcessorImpl")
@Slf4j
public class DispatchingOrderTaskOnSafeUpdateProcessorImpl implements Processor {

  @Resource
  DeliveredOrderService deliveredOrderService;

  @Resource
  DelayTaskService delayTaskService;

  @Resource
  OrderSuccessLogService orderSuccessLogService;

  @Resource(name = "safeServiceImpl")
  SafeService safeService;

  @Resource
  OrderService orderService;

  @Resource
  private WhiteShopFilter whiteShopFilter;

  @Override
  public boolean process(String message) {
    if (StringUtils.isBlank(message)) {
      LogUtils.error(log, "Message's blank");
      return true;
    }

    Safe safe;
    safe = JSON.parseObject(message, Safe.class);

    if (!whiteShopFilter.filterKdtId(safe.getKdtId())) {
      return true;
    }

    String orderNo = safe.getOrderNo();
    DelayTask
        orderTask =
        delayTaskService.getTaskByBizTypeAndBizId(BizType.DELIVERED_ORDER.code(), orderNo);

    if (orderTask == null) {
      LogUtils.warn(log, "OrderTask not found.safeNo={}", safe.getSafeNo());
      return true;
    }

    if (isFinished(safe)) {
      //恢复
      if (orderSuccessLogService.updateFinishTime(safe.getOrderNo(), safe.getUpdateTime())) {
        long suspendPeriod = orderSuccessLogService.getSuspendedPeriod(safe.getOrderNo(),
                                                                       orderTask.getDelayEndTime()
                                                                           .getTime());
        if (suspendPeriod <= 0) {
          LogUtils
              .error(log, "Invalid suspendPeriod={},safeNo={}", suspendPeriod, safe.getSafeNo());
        }
        delayTaskService.resumeTask(orderTask, suspendPeriod);
      }
    } else {
      if (isSuspendable(orderTask, safe)) {
        Order order = orderService.getOrderByOrderNoAndKdtId(orderNo, safe.getKdtId());
        if (order == null) {
          LogUtils.warn(log, "[SuspendTaskFail]Order not found.orderNo={},taskId={}", orderNo,
                         orderTask.getId());
        } else {
          orderSuccessLogService.addOrderSuccessLog(order, safe.getAddTime());
          delayTaskService.suspendTask(orderTask);
        }
      }
    }
    return true;
  }

  private boolean isSuspendable(DelayTask orderTask, Safe safe) {
    TaskStatus status = TaskStatus.getTaskStatusByCode(orderTask.getStatus());
    if (status == null) {
      LogUtils
          .error(log, "Invalid taskStatus={},taskId={}", orderTask.getStatus(), orderTask.getId());
      return false;
    }
    if (TaskStatus.ACTIVE == status) {
      SafeState safeState = SafeState.getSafeStateByCode(safe.getState());
      if (safeState != null) {
        if (safeState != SafeState.CLOSED && safeState != SafeState.FINISHED) {
          return true;
        }
      } else {
        LogUtils.error(log, "[Check suspendable]Invalid safeState={},safeNo={},taskId={}",
                       orderTask.getId());
      }
      return true;
    }
    return false;
  }

  /**
   * 根据维权记录当前状态来判断orderTask可以处于的状态
   */
  protected TaskStatus inferOrderTaskStatusBySafe(Safe safe) {

    if (safe.getState() == null || SafeState.getSafeStateByCode(safe.getState()) == null) {
      LogUtils
          .error(log, "[Infer]Invalid safeState={},safeNo={}", safe.getState(), safe.getSafeNo());
      return null;
    }
    //是否为新增，并且倒计时任务不为空：获取最新order_success_log记录。如果为空/finistime存在不为空，则调用沛大爷接口
    if (isNewlyAdded(safe)) {
      return TaskStatus.SUSPENDED;
    }
    //如果为结束，
    if (isFinished(safe)) {
      return TaskStatus.ACTIVE;
    }

    return null;
  }

  /**
   * 是否为维权结束.同个订单在同一时刻可能存在多笔维权。需要判断此刻是否还有其他维权处于处理中。
   */
  private boolean isFinished(Safe safe) {
    SafeState state = SafeState.getSafeStateByCode(safe.getState());
    if (SafeState.CLOSED == state || SafeState.FINISHED == state) {
      //判断是否有其他维权处于处理中
      boolean
          hasNoSafeOnTheGo =
          safeService.checkOrderFeedbackFinish(safe.getOrderNo(), safe.getKdtId());
      if (!hasNoSafeOnTheGo) {
        LogUtils.info(log, "Still has safe on the go.orderNo={},safeNo={}", safe.getOrderNo(),
                      safe.getSafeNo());
      }
      return hasNoSafeOnTheGo;
    }
    return false;
  }

  /**
   * 判断是否为新增维权操作
   *
   * @return 若为新增返回true
   */
  private boolean isNewlyAdded(Safe safe) {
    SafeState state = SafeState.getSafeStateByCode(safe.getState());
    if (SafeState.BUYER_START == state) {
      return true;
    }
    return false;
  }
}
