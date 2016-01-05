package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.OrderSuccessLog;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;


import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
public class DispatchingOrderTaskOnSafeUpdateProcessorImpl implements Processor{

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
    safe = JSON.parseObject(message, Safe.class);
    String orderNo = safe.getOrderNo();
    DelayTask
        orderTask =
        delayTaskService.getTaskByBizIdAndBizType(orderNo, BizType.DELIVERED_ORDER.code());

    if (orderTask == null) {
      LogUtils.info(log, "[Pass]OrderTask not found.safeNo={}", safe.getSafeNo());
      return true;
    }

    TaskStatus expectedStatus = inferOrderTaskStatusBySafe(safe);

    if (expectedStatus == null) {
      return true;
    }

    if(TaskStatus.ACTIVE.equals(expectedStatus)){
      //恢复
      if(orderSuccessLogService.updateFinishTime(safe.getOrderNo(),safe.getUpdateTime())){
        long suspendTime = orderSuccessLogService.getSuspendedTime(safe.getOrderNo(), orderTask.getDelayEndTime().getTime());
        delayTaskService.resumeTask(orderTask, suspendTime);
      }
    }
    return true;
  }

  /**
   * 根据维权记录当前状态来判断orderTask可以处于的状态
   */
  protected TaskStatus inferOrderTaskStatusBySafe(Safe safe) {

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
      return safeService.checkOrderFeedbackFinish(safe.getOrderNo(), safe.getKdtId());
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
