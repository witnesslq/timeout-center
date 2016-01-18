package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.timeout.handler.AbstractMsgTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author apple created at: 15/10/30 下午7:40
 */
@Component("safeMsgTaskHandlerImpl")
public class SafeMsgTaskHandlerImpl extends AbstractMsgTaskHandler {

  private String callPath = "trade.safe.timeout.sendMsg";

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
