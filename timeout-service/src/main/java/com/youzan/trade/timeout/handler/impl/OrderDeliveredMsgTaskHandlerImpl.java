package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.TaskResult;

import com.google.common.collect.Maps;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author apple created at: 16/1/4 上午12:29
 */
@Component("orderDeliveredMsgTaskHandlerImpl")
public class OrderDeliveredMsgTaskHandlerImpl extends AbstractMsgTaskHandler {

  private String callPath = "trade.order.countDown.smsAlert";

  @Async("defaultThreadPoolTaskExecutor")
  @Override
  public void handle(DelayTask delayTask) {
    Map<String, Object> params = Maps.newHashMap();
    generateParamsByDelayTask(delayTask, params);

    BaseResult<TaskResult> result = Client.call(getCallPath(),
                                                params,
                                                new TaskResult());

    if (handleDelayTaskByResponseCode(delayTask, result.getCode())) {
      return;
    }

    handleDelayTaskByResultCode(delayTask, result.getData().getResultCode());
  }

  private void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params) {
    params.put("order_no", delayTask.getBizId());
    params.put("kdt_id", delayTask.getBizShardKey());
  }

  @Override
  protected String getCallPath() {
    return this.callPath;
  }
}
