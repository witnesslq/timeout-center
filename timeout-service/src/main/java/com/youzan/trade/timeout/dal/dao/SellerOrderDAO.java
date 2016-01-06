package com.youzan.trade.timeout.dal.dao;

import com.youzan.trade.timeout.dal.dataobject.OrderDO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * 卖家订单相关DAO操作
 *
 * @author wangxiaolei
 */
public interface SellerOrderDAO {

  /**
   *
   * 通过orderNo查找唯一对应的OrderDO
   *
   * @param orderNo
   * @param kdtId 路由使用
   * @return
   */
  OrderDO queryByOrderNo(@Param("orderNo") String orderNo, @Param("kdtId") int kdtId);

  /**
   *
   *  获取订单状态.
   *
   * @param orderNo
   * @param kdtId
   * @return
   */
  Integer getOrderState(@Param("orderNo") String orderNo, @Param("kdtId") int kdtId);

  /**
   * 根据订单号列表查询订单
   *
   * @param orderNoList
   * @param kdtId
   * @return
   */
  List<OrderDO> queryByOrderNoListAndKdtId(@Param("orderNoList") List<String> orderNoList,
                                           @Param("kdtId") int kdtId);
}
