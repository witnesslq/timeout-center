package com.youzan.trade.timeout.order.service;

import com.youzan.trade.timeout.entities.Order;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
public interface DeliveredOrderService {

  /***
   *
   * */
  boolean addToDelayTask(Order order);
}
