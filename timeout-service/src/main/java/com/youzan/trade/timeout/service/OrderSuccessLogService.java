package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.dal.dataobject.OrderSuccessLogDO;
import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.model.OrderSuccessLog;

/**
 * Created by hupeipei on 15/12/29.
 */
public interface OrderSuccessLogService {

  OrderSuccessLog getLatestOrderSuccessLogByOrderNo(String orderNo);

  boolean updateFinishTime(String OrderNo, int finishTime);

  boolean addOrderSuccessLog(Order order, int safeTime);

}
