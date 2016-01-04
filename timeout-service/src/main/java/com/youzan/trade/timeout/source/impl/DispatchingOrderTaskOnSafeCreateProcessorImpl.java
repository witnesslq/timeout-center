package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

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

    orderSuccessLogService.addOrderSuccessLog();
    delayTaskService.suspendTask(orderTask);
    return true;
  }
}
