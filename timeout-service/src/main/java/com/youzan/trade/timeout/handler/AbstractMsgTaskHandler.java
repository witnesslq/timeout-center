package com.youzan.trade.timeout.handler;

import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import javax.annotation.Resource;

/**
 * @author apple created at: 16/1/5 下午3:40
 */
public abstract class AbstractMsgTaskHandler implements TaskHandler {

  @Resource
  protected DelayTaskService delayTaskService;

  private void handleOnSuccess(DelayTask delayTask) {
    delayTaskService.closeMsgOnSuccess(delayTask.getId());
  }

  private void handleOnNoRetry(DelayTask delayTask) {
    delayTaskService.closeMsgOnNoRetry(delayTask.getId());
  }

  private void handleOnRetry(DelayTask delayTask) {
    delayTaskService.updateMsgOnRetry(delayTask.getId());
  }

  protected abstract String getCallPath();

  protected void handleDelayTaskByResultCode(DelayTask delayTask, Integer resultCode) {
    switch (resultCode) {
      case Constants.MSG_TASK_SUCCESS:
        handleOnSuccess(delayTask);
        break;

      case Constants.MSG_TASK_FAILURE_RETRY:
        handleOnRetry(delayTask);
        break;

      case Constants.MSG_TASK_FAILURE_NO_RETRY:
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
