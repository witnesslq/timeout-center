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
 * @author apple created at: 15/10/30 下午7:40
 */
@Component("safeMsgTaskHandlerImpl")
public class SafeMsgTaskHandlerImpl implements TaskHandler {

  @Resource
  private DelayTaskService delayTaskService;

  @Override
  public void handle(DelayTask delayTask) {
      Map<String, Object> params = Maps.newHashMap();
      params.put("safe_no", delayTask.getBizId());
      params.put("state", delayTask.getBizState());

      BaseResult<SafeTaskResult> result = Client.call("trade.safe.timeout.sendMsg",
                                                      params,
                                                      new SafeTaskResult());

      if (result == null) {
        handleOnRetry(delayTask);
        return ;
      }

      if (ResponseCode.SUCC != result.getCode()) {
        handleOnRetry(delayTask);
        return ;
      }

      switch (result.getData().getResultCode()) {
        case Constants.SAFE_MSG_TASK_SUCCESS:
          handleOnSuccess(delayTask);
          break;

        case Constants.SAFE_MSG_TASK_FAILURE_RETRY:
          handleOnRetry(delayTask);
          break;

        case Constants.SAFE_MSG_TASK_FAILURE_NO_RETRY:
          handleOnNoRetry(delayTask);
          break;

        default : handleOnNoRetry(delayTask);
      }
  }

  private void handleOnSuccess(DelayTask delayTask) {
    delayTaskService.closeMsgOnSuccess(delayTask.getId());
  }

  private void handleOnNoRetry(DelayTask delayTask) {
    delayTaskService.closeMsgOnNoRetry(delayTask.getId());
  }

  private void handleOnRetry(DelayTask delayTask) {
    delayTaskService.updateMsgOnRetry(delayTask.getId());
  }
}
