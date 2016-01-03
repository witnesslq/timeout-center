package com.youzan.trade.timeout.source.impl;

import com.youzan.platform.util.json.JsonUtil;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听维权消息，区分维权出于那种状态，来对deliveredOrderTask进行中断/恢复处理
 *
 * @author liwenjia created at: 15/12/31
 */
@Component(value = "dispatchingOrderTaskOnSafeProcessorImpl")
@Slf4j
public class DispatchingOrderTaskOnSafeProcessorImpl implements Processor {

  @Resource
  DeliveredOrderService deliveredOrderService;

  @Resource
  DelayTaskService delayTaskService;

  @Resource
  OrderSuccessLogService orderSuccessLogService;

  @Resource(name = "safeServiceImpl")
  SafeService safeService;

  @Override
  public boolean process(String message) {
    if (StringUtils.isBlank(message)) {
      LogUtils.warn(log, "Message's blank");
      return true;
    }

    Safe safe;
    try {
      safe = JsonUtil.json2obj(message, Safe.class);
    } catch (IOException e) {
      LogUtils.error(log, "Parse message failed.message=" + message, e);
      return true;
    }
    String orderNo = safe.getOrderNo();
    DelayTask
        orderTask =
        delayTaskService.getTaskByBizIdAndBizType(orderNo, BizType.DELIVERED_ORDER.code());

    if (orderTask == null) {
      LogUtils.info(log, "[Pass]OrderTask not found.safeNo={}", safe.getSafeNo());
      return true;
    }

    TaskStatus expectedStatus = inferOrderTaskStatusOnSafe(safe);

    if (expectedStatus == null) {
      return true;
    }

    switch (expectedStatus) {
      case ACTIVE:
        //恢复
        return delayTaskService.resumeTask(orderTask);
      case SUSPENDED:
        //中断
        return delayTaskService.suspendTask(orderTask);
      case CLOSED:
        //关闭
        LogUtils.info(log, "超时任务提前关闭.taskId={}", orderTask.getId());
        return delayTaskService.closeOnNoRetry(orderTask.getId());
      default:
        LogUtils.warn(log, "No need to process orderTask.safeNo={}", safe.getSafeNo());
        return true;
    }
  }

  /**
   * 根据维权记录当前状态来判断orderTask可以处于的状态 TODO
   */
  private TaskStatus inferOrderTaskStatusOnSafe(Safe safe) {

    if (safe.getState() == null || SafeState.getSafeStateByCode(safe.getState()) == null) {
      LogUtils.error(log, "Invalid safe state={},safeNo={}", safe.getState(), safe.getSafeNo());
      return null;
    }
    //是否为新增，并且倒计时任务不为空：获取最新order_success_log记录。如果为空/finistime存在不为空，则调用沛大爷接口
    if(isAdded(safe)){
      return TaskStatus.SUSPENDED;
    }
    //如果为结束，
    if(isFinished(safe)){
      return TaskStatus.ACTIVE;
    }

    return null;
  }

  /**
   * 是否为维权结束.同个订单在同一时刻可能存在多比维权。需要判断此刻是否还有其他维权处于处理中。
   * @param safe
   * @return
   */
  private boolean isFinished(Safe safe) {
    SafeState state = SafeState.getSafeStateByCode(safe.getState());
    if(SafeState.CLOSED==state || SafeState.FINISHED==state){
      //判断是否有其他维权处于处理中
      return safeService.checkOrderFeedbackFinish(safe.getOrderNo(),safe.getKdtId());
    }
    return false;
  }

  /**
   * 判断是否为新增维权操作
   * @param safe
   * @return 若为新增返回true
   */
  private boolean isAdded(Safe safe) {
    SafeState state = SafeState.getSafeStateByCode(safe.getState());
    if(SafeState.BUYER_START == state){
      return true;
    }
    return false;
  }

}
