package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.dal.dao.OrderSuccessLogDAO;
import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;
import com.youzan.trade.timeout.service.AbstractOrderRelatedDelayTimeStrategy;
import com.youzan.trade.timeout.service.OrderSuccessLogService;
import com.youzan.trade.timeout.transfer.OrderSuccessLogTransfer;
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by hupp on 15/12/29.
 */
@Component
@Slf4j
public class OrderSuccessLogServiceImpl implements OrderSuccessLogService {

  @Resource
  private OrderSuccessLogDAO orderSuccessLogDAO;

  @Resource(name = "autoCompleteTaskDelayTimeStrategyImpl")
  AbstractOrderRelatedDelayTimeStrategy delayTimeStrategy;

  @Override
  public OrderSuccessLog getLatestOrderSuccessLogByOrderNo(String orderNo) {
    OrderSuccessLogDO orderSuccessLogDO  = orderSuccessLogDAO.getLastOrderSuccessLogByOrderNo(orderNo);
    return OrderSuccessLogTransfer.transfer2TO(orderSuccessLogDO);
  }

  @Override
  public boolean updateFinishTime(String orderNo, int finishTime) {
    OrderSuccessLog orderSuccessLog = getLatestOrderSuccessLogByOrderNo(orderNo);
    if (orderSuccessLog == null) {
      LogUtils
          .error(log, "OrderSuccessLog not found.orderNo={},finishTime={}", orderNo, finishTime);
      return false;
    }
    if (finishTime <= 0) {
      LogUtils.error(log, "finish time should be positive.orderNo={},finishTime={}", orderNo,
                     finishTime);
      return false;
    }
    orderSuccessLog.setFinishTime(finishTime);
    return updateFinishTimeAndSuspendedPeriod(orderSuccessLog);
  }

  @Override
  public boolean updateFinishTimeAndSuspendedPeriod(OrderSuccessLog orderSuccessLog) {
    int suspendedPeriod = orderSuccessLog.getFinishTime() - orderSuccessLog.getCreateTime();
    return orderSuccessLogDAO
        .updateFinishTimeByOrderNo(orderSuccessLog.getId(), orderSuccessLog.getOrderNo(),
                                   orderSuccessLog.getFinishTime(),
                                   suspendedPeriod);
  }

  @Override
  public boolean addOrderSuccessLog(Order order, int safeTime) {
    if (order == null || safeTime == 0) {
      return false;
    }
    OrderSuccessLog orderSuccessLog = getLatestOrderSuccessLogByOrderNo(order.getOrderNo());
    if (orderSuccessLog == null || orderSuccessLog.getFinishTime() != null) {
      int delayTime = delayTimeStrategy.getInitialDelayTimeByOrderType(
          BizType.DELIVERED_ORDER.code(), order.getOrderType(), order.getExpressTime());
      int remainTime = order.getExpressTime() + delayTime - safeTime;
      OrderSuccessLog buildOrderSuccessLog = buildOrderSuccessLog(order, remainTime);
      return orderSuccessLogDAO.insert(
          OrderSuccessLogTransfer.transfer2DO(buildOrderSuccessLog));
    }
    return true;
  }

  @Override
  public long getSuspendedPeriod(String orderNo, long originTaskEndTime) {
    OrderSuccessLog orderSuccessLog = this.getLatestOrderSuccessLogByOrderNo(orderNo);
    return orderSuccessLog.getSuspendedPeriod()*1000L; //second to millisecond
  }

  private OrderSuccessLog buildOrderSuccessLog(Order order, int remainTime) {
    int currentTime = TimeUtils.currentInSeconds();
    OrderSuccessLog orderSuccessLog = new OrderSuccessLog();
    orderSuccessLog.setOrderNo(order.getOrderNo());
    orderSuccessLog.setKdtId(order.getKdtId());
    orderSuccessLog.setCreateTime(currentTime);
    orderSuccessLog.setRemainTime(remainTime);
    orderSuccessLog.setSuspendedPeriod(0);
    return orderSuccessLog;
  }

}
