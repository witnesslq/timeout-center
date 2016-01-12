package com.youzan.trade.timeout.transfer;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.model.OrderSuccessLog;

/**
 * @author Created by hupp on 2015/12/29 .
 */
public class OrderSuccessLogTransfer {

  public static OrderSuccessLog transfer2TO(OrderSuccessLogDO orderSuccessLogDO) {
    if (orderSuccessLogDO == null) {
      return null;
    }
    OrderSuccessLog orderSuccessLog = new OrderSuccessLog();
    orderSuccessLog.setId(orderSuccessLogDO.getId());
    orderSuccessLog.setOrderNo(orderSuccessLogDO.getOrderNo());
    orderSuccessLog.setKdtId(orderSuccessLogDO.getKdtId());
    orderSuccessLog.setCreateTime(orderSuccessLogDO.getCreateTime());
    orderSuccessLog.setFinishTime(orderSuccessLogDO.getFinishTime());
    orderSuccessLog.setRemainTime(orderSuccessLogDO.getRemainTime());
    orderSuccessLog.setSuspendedPeriod(orderSuccessLogDO.getSuspendedPeriod());
    return orderSuccessLog;
  }

  public static OrderSuccessLogDO transfer2DO(OrderSuccessLog orderSuccessLog) {
    if (orderSuccessLog == null) {
      return null;
    }
    OrderSuccessLogDO orderSuccessLogDO = new OrderSuccessLogDO();
    orderSuccessLogDO.setId(orderSuccessLog.getId());
    orderSuccessLogDO.setOrderNo(orderSuccessLog.getOrderNo());
    orderSuccessLogDO.setKdtId(orderSuccessLog.getKdtId());
    orderSuccessLogDO.setCreateTime(orderSuccessLog.getCreateTime());
    orderSuccessLogDO.setFinishTime(orderSuccessLog.getFinishTime());
    orderSuccessLogDO.setRemainTime(orderSuccessLog.getRemainTime());
    orderSuccessLogDO.setSuspendedPeriod(orderSuccessLog.getSuspendedPeriod());
    return orderSuccessLogDO;
  }
}

