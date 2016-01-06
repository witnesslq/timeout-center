package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.model.Order;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/04 .
 */
public interface OrderService {

  Order getOrderByOrderNoAndKdtId(String orderNo, Integer kdtId);
}
