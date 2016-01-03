package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.dal.dao.OrderSuccessLogDAO;
import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.transfer.OrderSuccessLogTransfer;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by hupp on 15/12/29.
 */
@Component
public class OrderSuccessLogServiceImpl implements OrderSuccessLogService {



  @Resource
  private OrderSuccessLogDAO orderSuccessLogDAO;

  //TODO
  @Resource(name = "")
  DelayTimeStrategy delayTimeStrategy;

  @Override
  public OrderSuccessLog getLatestOrderSuccessLogByOrderNo(String orderNo) {
    OrderSuccessLogDO orderSuccessLogDO  = orderSuccessLogDAO.getLastOrderSuccessLogByOrderNo(orderNo);
    return OrderSuccessLogTransfer.transfer2TO(orderSuccessLogDO);
  }

  @Override
  public boolean updateFinishTime(String OrderNo, int finishTime) {
    if (finishTime == 0) {
      return false;
    }
    return orderSuccessLogDAO.updateFinishTimeByOrderNo(OrderNo, finishTime) == true;
  }

  @Override
  public boolean addOrderSuccessLog(Order order, int safeTime) {
    if (order == null || safeTime == 0) {
      return false;
    }
    OrderSuccessLog orderSuccessLog = getLatestOrderSuccessLogByOrderNo(order.getOrderNo());
    if (orderSuccessLog == null || orderSuccessLog.getFinishTime() != null) {
      int delayTime = delayTimeStrategy.getInitialDelayTime(BizType.DELIVERED_ORDER.code(),order,);//普通和批发的t+?
      int remainTime = order.getExpressTime() + delayTime - safeTime;
      OrderSuccessLog buildOrderSuccessLog = buildOrderSuccessLog(order, remainTime);
      return orderSuccessLogDAO.insert(
          OrderSuccessLogTransfer.transfer2DO(buildOrderSuccessLog)) == true;
    }
    return true;
  }

  private OrderSuccessLog buildOrderSuccessLog(Order order, int remainTime) {
    int currentTime = TimeUtils.currentInSeconds();
    OrderSuccessLog orderSuccessLog = new OrderSuccessLog();
    orderSuccessLog.setOrderNo(order.getOrderNo());
    orderSuccessLog.setKdtId(order.getKdtId());
    orderSuccessLog.setCreateTime(currentTime);
    orderSuccessLog.setRemainTime(remainTime);
    return orderSuccessLog;
  }

}
