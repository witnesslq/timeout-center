package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;

/**
 * Created by hupeipei on 15/12/29.
 */
public interface OrderSuccessLogService {

  OrderSuccessLog getLatestOrderSuccessLogByOrderNo(Order order);

  boolean updateOrderSuccessLog(OrderSuccessLog orderSuccessLog);

  boolean insetOrderSuccessLog(OrderSuccessLog orderSuccessLo);

}
