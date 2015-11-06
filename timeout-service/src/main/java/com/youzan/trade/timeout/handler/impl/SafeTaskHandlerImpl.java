package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.SafeTaskResult;
import com.youzan.trade.timeout.service.DelayTaskService;

import com.google.common.collect.Maps;

import org.springframework.stereotype.Component;

import java.util.Map;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/23 下午8:20
 */
@Component("safeTaskHandlerImpl")
public class SafeTaskHandlerImpl implements TaskHandler {

  @Resource
  private DelayTaskService delayTaskService;

  @Override
  public void handle(DelayTask delayTask) {
    Map<String, Object> params = Maps.newHashMap();
    params.put("safe_no", delayTask.getBizId());
    params.put("state", delayTask.getBizState());

    BaseResult<SafeTaskResult> result = Client.call("trade.safe.timeout.execute",
                                                    params,
                                                    new SafeTaskResult());

    if (ResponseCode.SUCC != result.getCode()) {
      handleOnRetry(delayTask);
      return ;
    }

    switch (result.getData().getResultCode()) {
      case Constants.SAFE_TASK_SUCCESS:
        handleOnSuccess(delayTask);
        break;

      case Constants.SAFE_TASK_FAILURE_RETRY:
        handleOnRetry(delayTask);
        break;

      case Constants.SAFE_TASK_FAILURE_NO_RETRY:
        handleOnNoRetry(delayTask);
        break;

      default : handleOnNoRetry(delayTask);
    }
  }

  private void handleOnSuccess(DelayTask delayTask) {
    delayTaskService.closeOnSuccess(delayTask.getId());
  }

  private void handleOnNoRetry(DelayTask delayTask) {
    delayTaskService.closeOnNoRetry(delayTask.getId());
  }

  private void handleOnRetry(DelayTask delayTask) {
    delayTaskService.updateOnRetry(delayTask.getId());
  }
}
