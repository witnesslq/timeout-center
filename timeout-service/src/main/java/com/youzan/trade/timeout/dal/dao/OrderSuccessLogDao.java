package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
public interface OrderSuccessLogDAO {

  /***
   * 新增一条记录
   * **/
  boolean insert(OrderSuccessLogDO orderSuccessDO);

  /**
   * 获取最新一条记录
   * ***/
  OrderSuccessLogDAO getLatestOrderSuccessLogByOrderNo(Order order);

  /***
   * 更新记录
   * */
  boolean updateOrderSuccesLog(OrderSuccessLogDO orderSuccessDO);

  boolean insetOrderSuccessLog(OrderSuccessLog orderSuccessLo);
}
