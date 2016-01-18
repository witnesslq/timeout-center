package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.timeout.handler.AbstractTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author apple created at: 15/10/23 下午8:20
 */
@Component("safeTaskHandlerImpl")
public class SafeTaskHandlerImpl extends AbstractTaskHandler {

  private String callPath = "trade.safe.timeout.execute";

  @Override
  protected void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params) {
    params.put("safe_no", delayTask.getBizId());
    params.put("state", delayTask.getBizState());
  }

  @Override
  protected String getCallPath() {
    return this.callPath;
  }

}
