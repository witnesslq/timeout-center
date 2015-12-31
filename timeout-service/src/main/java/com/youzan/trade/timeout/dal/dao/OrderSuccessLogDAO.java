package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;

import org.apache.ibatis.annotations.Param;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
public interface OrderSuccessLogDAO {

  /***
   * 新增一条记录
   */
  boolean insert(OrderSuccessLogDO orderSuccessDO);

  /**
   * 获取最新一条记录
   */
  OrderSuccessLogDO getLastOrderSuccessLogByOrderNo(@Param("orderNo") String orderNo);

  /**
   * 更新完成时间
   */
  boolean updateFinishTimeByOrderNo(@Param("orderNo") String orderNo, @Param("finishTime") int finishTime);


}
