package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/12/30 下午3:21
 */
public abstract class AbstractTaskHandler implements TaskHandler {

  @Resource
  protected DelayTaskService delayTaskService;

  private void handleOnSuccess(DelayTask delayTask) {
    delayTaskService.closeOnSuccess(delayTask.getId());
  }

  private void handleOnNoRetry(DelayTask delayTask) {
    delayTaskService.closeOnNoRetry(delayTask.getId());
  }

  private void handleOnRetry(DelayTask delayTask) {
    delayTaskService.updateOnRetry(delayTask.getId());
  }

  protected void handleDelayTaskByResultCode(DelayTask delayTask, Integer resultCode) {
    switch (resultCode) {
      case Constants.TASK_SUCCESS:
        handleOnSuccess(delayTask);
        break;

      case Constants.TASK_FAILURE_RETRY:
        handleOnRetry(delayTask);
        break;

      case Constants.TASK_FAILURE_NO_RETRY:
        handleOnNoRetry(delayTask);
        break;

      default : handleOnNoRetry(delayTask);
    }
  }

  protected boolean handleDelayTaskByResponseCode(DelayTask delayTask, int responseCode) {
    if (ResponseCode.SUCC != responseCode) {
      handleOnRetry(delayTask);
      return true;
    }
    return false;
  }
}
