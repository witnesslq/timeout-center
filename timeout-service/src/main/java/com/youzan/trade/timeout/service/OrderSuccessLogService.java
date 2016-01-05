package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;

/**
 * 用来计算剩余时间
 * Created by hupeipei on 15/12/29.
 */
public interface OrderSuccessLogService {

  OrderSuccessLog getLatestOrderSuccessLogByOrderNo(String orderNo);

  boolean updateFinishTime(String OrderNo, int finishTime);

  boolean updateFinishAndRemainTime(OrderSuccessLog orderSuccessLog);

  boolean addOrderSuccessLog(Order order, int safeTime);

  long getSuspendedPeriod(String orderNo, long originTaskEndTime);
}
