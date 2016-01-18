package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.timeout.handler.AbstractTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author apple created at: 15/12/30 下午3:18
 */
@Component("orderDeliveredTaskHandlerImpl")
public class OrderDeliveredTaskHandlerImpl extends AbstractTaskHandler {

  private String callPath = "trade.order.countDown.completeOrder";

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
