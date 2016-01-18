package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.timeout.handler.AbstractMsgTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author apple created at: 16/1/4 上午12:29
 */
@Component("orderDeliveredMsgTaskHandlerImpl")
public class OrderDeliveredMsgTaskHandlerImpl extends AbstractMsgTaskHandler {

  private String callPath = "trade.order.countDown.smsAlert";

  @Override
  protected void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params) {
    params.put("order_no", delayTask.getBizId());
    params.put("kdt_id", delayTask.getBizShardKey());
  }

  @Override
  protected String getCallPath() {
    return this.callPath;
  }
}
