package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.OrderState;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.timeout.service.OrderService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听维权创建，区分维权处于那种状态，来对deliveredOrderTask进行中断/恢复处理
 *
 * @author liwenjia created at: 15/12/31
 */
@Component(value = "dispatchingOrderTaskOnSafeCreateProcessorImpl")
@Slf4j
public class DispatchingOrderTaskOnSafeCreateProcessorImpl implements Processor {

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
    Order order = orderService.getOrderByOrderNoAndKdtId(safe.getOrderNo(), safe.getKdtId());
    if (isSuspendableOrder(orderTask,safe)) {
      orderSuccessLogService.addOrderSuccessLog(order, safe.getAddTime());
      delayTaskService.suspendTask(orderTask);
    } else {
      LogUtils.warn(log, "Order's null or order not in sent state.order={}",
                    order == null ? null : JSON.toJSON(order));
    }
    return true;
  }

  private boolean isSuspendableOrder(DelayTask orderTask, Safe safe) {
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
        LogUtils.error(log, "Invalid safeState={},safeNo={},taskId={}", orderTask.getId());
      }
      return true;
    }
    return false;
  }
}
