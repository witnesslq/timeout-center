package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.TaskResult;

import com.google.common.collect.Maps;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author apple created at: 15/10/23 下午8:20
 */
@Component("safeTaskHandlerImpl")
public class SafeTaskHandlerImpl extends AbstractTaskHandler {

  @Async("safeThreadPoolTaskExecutor")
  @Override
  public void handle(DelayTask delayTask) {
    Map<String, Object> params = Maps.newHashMap();
    params.put("safe_no", delayTask.getBizId());
    params.put("state", delayTask.getBizState());

    BaseResult<TaskResult> result = Client.call("trade.safe.timeout.execute",
                                                    params,
                                                    new TaskResult());

    if (handleDelayTaskByResponseCode(delayTask, result.getCode())) {
      return;
    }

    handleDelayTaskByResultCode(delayTask, result.getData().getResultCode());
  }

}
