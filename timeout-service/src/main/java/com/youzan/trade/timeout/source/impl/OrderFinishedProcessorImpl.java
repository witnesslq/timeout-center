package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by hupp on 2016/01/07 .
 */
@Slf4j
@Component(value = "orderFinishedProcessorImpl")
public class OrderFinishedProcessorImpl implements Processor {

  @Resource(name = "deliveredOrderServiceImpl")
  DeliveredOrderService deliveredOrderService;

  @Override
  public boolean process(String message) {
    if (StringUtils.isBlank(message)) {
      LogUtils.warn(log, "Order Finished Message's blank");
      return true;
    }
    Order order = JSON.parseObject(message, Order.class);

    return deliveredOrderService.closeDelayTask(order);
  }
}
