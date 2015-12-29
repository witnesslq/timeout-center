package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessDO;
import com.youzan.trade.timeout.entities.Order;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
public interface OrderSuccessLogDAO {

  /**
   * 获取最新一条记录
   * ***/
  OrderSuccessLogDAO getLatestOrderSuccessLogByOrderNo(Order order);

  /***
   * 更新记录
   * */
  boolean updateOrderSuccesLog(OrderSuccessDO orderSuccessDO);
}
