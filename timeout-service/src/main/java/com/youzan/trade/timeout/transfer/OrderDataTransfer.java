package com.youzan.trade.timeout.transfer;


import com.youzan.trade.timeout.dal.dataobject.ChildOrderDO;
import com.youzan.trade.timeout.dal.dataobject.OrderDO;
import com.youzan.trade.timeout.model.Order;

import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by apple on 15/9/15.
 */
public class OrderDataTransfer {
  
  /**
   *
   * TO -> DO 由于mysql数据库表中做了字段not null限制。
   *
   * 所以在转为DO的时候设置默认值.
   *
   * @param order
   * @return
   */
  public static OrderDO transfer2DO(Order order) {
    if (order == null) {
      return null;
    }

    OrderDO orderDO = new OrderDO();
    orderDO.setOrderNo(order.getOrderNo());
    orderDO.setKdtId(order.getKdtId());
    orderDO.setCustomerId(order.getCustomerId());
    orderDO.setCustomerName(order.getCustomerName());
    orderDO.setCustomerType(order.getCustomerType());
    orderDO.setBuyerId(order.getBuyerId());
    orderDO.setBuyerPhone(order.getBuyerPhone());
    orderDO.setTourist(order.getTourist());
    orderDO.setState(order.getOrderState());
    orderDO.setStockState(order.getStockState());
    orderDO.setPayState(order.getPayState());
    orderDO.setExpressState(order.getExpressState());
    orderDO.setFeedback(order.getFeedback());
    orderDO.setRefundState(order.getRefundState());
    orderDO.setCloseState(order.getCloseState());
    orderDO.setOrderType(order.getOrderType());
    orderDO.setExpressType(order.getExpressType());
    orderDO.setBuyWay(order.getBuyWay());
    orderDO.setNormal(order.getNormal());
    orderDO.setHasChild(order.getHasChild());
    orderDO.setUpdateTime(order.getUpdateTime());
    orderDO.setBookTime(order.getBookTime());
    orderDO.setExpireTime(order.getExpireTime());
    orderDO.setPayTime(order.getPayTime());
    orderDO.setExpressTime(order.getExpressTime());
    orderDO.setFeedbackTime(order.getFeedbackTime());
    orderDO.setRefundTime(order.getRefundTime());
    orderDO.setSuccessTime(order.getSuccessTime());
    orderDO.setCloseTime(order.getCloseTime());
    orderDO.setCurrency(order.getCurrency());

    return orderDO;
  }

  public static Order transfer2TO(OrderDO orderDO) {
    if (orderDO == null) {
      return null;
    }

    Order order = new Order();
    order.setOrderNo(orderDO.getOrderNo());
    order.setKdtId(orderDO.getKdtId());
    order.setCustomerId(orderDO.getCustomerId());
    order.setCustomerName(orderDO.getCustomerName());
    order.setCustomerType(orderDO.getCustomerType());
    order.setBuyerId(orderDO.getBuyerId());
    order.setBuyerPhone(orderDO.getBuyerPhone());
    order.setTourist(orderDO.getTourist());
    order.setOrderState(orderDO.getState());
    order.setStockState(orderDO.getStockState());
    order.setPayState(orderDO.getPayState());
    order.setExpressState(orderDO.getExpressState());
    order.setFeedback(orderDO.getFeedback());
    order.setRefundState(orderDO.getRefundState());
    order.setCloseState(orderDO.getCloseState());
    order.setOrderType(orderDO.getOrderType());
    order.setExpressType(orderDO.getExpressType());
    order.setBuyWay(orderDO.getBuyWay());
    order.setNormal(orderDO.getNormal());
    order.setHasChild(orderDO.getHasChild());
    order.setUpdateTime(orderDO.getUpdateTime());
    order.setBookTime(orderDO.getBookTime());
    order.setExpireTime(orderDO.getExpireTime());
    order.setPayTime(orderDO.getPayTime());
    order.setExpressTime(orderDO.getExpressTime());
    order.setFeedbackTime(orderDO.getFeedbackTime());
    order.setRefundTime(orderDO.getRefundTime());
    order.setSuccessTime(orderDO.getSuccessTime());
    order.setCloseTime(orderDO.getCloseTime());
    order.setCurrency(orderDO.getCurrency());

    return order;
  }

  public static List<Order> transfer2TOList(List<OrderDO> orderDOList) {
    if (CollectionUtils.isEmpty(orderDOList)) {
      return Collections.emptyList();
    }

    List<Order> orderList = new ArrayList<>();
    orderDOList.forEach(orderDO -> orderList.add(transfer2TO(orderDO)));

    return orderList;
  }

  //child_order转化为order，实现对外暴露
  public static Order transfer2TOFromChild(ChildOrderDO childOrderDO) {
    if (childOrderDO == null)
      return null;

    Order order = new Order();
    //important column: id
    order.setId(childOrderDO.getId());
    order.setOrderNo(childOrderDO.getOrderNo());
    order.setKdtId(childOrderDO.getKdtId());
    order.setCustomerId(childOrderDO.getCustomerId());
    order.setCustomerName(childOrderDO.getCustomerName());
    order.setCustomerType(childOrderDO.getCustomerType());
    order.setBuyerId(childOrderDO.getBuyerId());
    order.setBuyerPhone(childOrderDO.getBuyerPhone());
    order.setTourist(childOrderDO.getTourist());
    order.setOrderState(childOrderDO.getState());
    order.setStockState(childOrderDO.getStockState());

    order.setFeedback(childOrderDO.getFeedback());

    order.setCloseState(childOrderDO.getCloseState());

    order.setExpressType(childOrderDO.getExpressType());
    order.setBuyWay(childOrderDO.getBuyWay());
    order.setNormal(childOrderDO.getNormal());

    order.setUpdateTime(childOrderDO.getUpdateTime());
    order.setBookTime(childOrderDO.getBookTime());
    order.setExpireTime(childOrderDO.getExpireTime());
    order.setPayTime(childOrderDO.getPayTime());
    order.setExpressTime(childOrderDO.getExpressTime());
    order.setFeedbackTime(childOrderDO.getFeedbackTime());

    order.setCloseTime(childOrderDO.getCloseTime());

    return order;
  }

  public static List<Order> transfer2TOFromChild(List<ChildOrderDO> childOrderDOs) {
    if (CollectionUtils.isEmpty(childOrderDOs))
      return Collections.emptyList();

    List<Order> orders = Lists.newArrayList();
    childOrderDOs.forEach(childOrderDO -> orders.add(transfer2TOFromChild(childOrderDO)));
    return orders;
  }
}
