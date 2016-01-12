package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.dal.dao.SellerOrderDAO;
import com.youzan.trade.timeout.dal.dataobject.OrderDO;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.service.OrderService;
import com.youzan.trade.timeout.transfer.OrderDataTransfer;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/04 .
 */
@Component(value = "orderService")
public class OrderServiceImpl implements OrderService{

  @Resource
  SellerOrderDAO sellerOrderDAO;

  @Override
  public Order getOrderByOrderNoAndKdtId(String orderNo, Integer kdtId) {
    OrderDO orderDO = sellerOrderDAO.queryByOrderNo(orderNo, kdtId);
    return OrderDataTransfer.transfer2TO(orderDO);
  }
}
