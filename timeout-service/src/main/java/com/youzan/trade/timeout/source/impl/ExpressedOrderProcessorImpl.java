package com.youzan.trade.timeout.source.impl;

import com.youzan.platform.util.json.JsonUtil;
import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;

import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单出于已发货状态订单增加倒计时任务
 *
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Component
@Slf4j
public class ExpressedOrderProcessorImpl implements Processor {

  @Resource
  DeliveredOrderService expressedOrderService;

  @Override
  public boolean process(String message) {
    Order order = null;
    try {
      order = JsonUtil.json2obj(message, Order.class);
      expressedOrderService.addToDelayTask(order);
    } catch (IOException e) {
      LogUtils.error(log, "Process message failed.message={}", message, e);
    }

    return false;
  }
}
