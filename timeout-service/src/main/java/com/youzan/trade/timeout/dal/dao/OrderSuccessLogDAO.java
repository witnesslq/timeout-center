package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;

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
   *
   * @param orderNo
   * @param finishTime
   * @param suspendedPeriod
   * @return
   */
  boolean updateFinishTimeByOrderNo(@Param("id")Integer id,
                                    @Param("orderNo") String orderNo,
                                    @Param("finishTime") int finishTime,
                                    @Param("suspendedPeriod") int suspendedPeriod);
}
