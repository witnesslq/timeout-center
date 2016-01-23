package com.youzan.trade.timeout.handler;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.TaskResult;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.util.LogUtils;

import com.google.common.collect.Maps;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author apple created at: 16/1/5 下午3:40
 */
@Slf4j
public abstract class AbstractMsgTaskHandler implements TaskHandler {

  @Resource
  protected DelayTaskService delayTaskService;

  @Async("taskExecutor")
  @Override
  public void handle(DelayTask delayTask) {
    LogUtils.info(log, "开始处理消息任务, taskId: {}", delayTask.getId());
    long startTime = System.currentTimeMillis();
    /**
     * 先尝试获取锁
     * 如果获取不到,则什么都不做
     */
    if (!delayTaskService.lockMsgTaskByTaskId(delayTask.getId())) {
      LogUtils.info(log, "处理消息任务, 获取消息任务锁失败, taskId: {}", delayTask.getId());
      return;
    }

    try {
      Map<String, Object> params = Maps.newHashMap();
      generateParamsByDelayTask(delayTask, params);

      BaseResult<TaskResult> result = Client.call(getCallPath(),
                                                  params,
                                                  new TaskResult());

      if (handleDelayTaskByResponseCode(delayTask, result.getCode())) {
        return;
      }

      handleDelayTaskByResultCode(delayTask, result.getData().getResultCode());
    } finally {
      /**
       * 最后释放锁
       */
      delayTaskService.unlockMsgTaskByTaskId(delayTask.getId());
      LogUtils.info(log, "任务处理共耗时: {} ms, taskId: {}",
                    System.currentTimeMillis() - startTime, delayTask.getId());
    }

  }

  protected abstract void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params);

  protected abstract String getCallPath();

  private void handleDelayTaskByResultCode(DelayTask delayTask, Integer resultCode) {
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

  private boolean handleDelayTaskByResponseCode(DelayTask delayTask, int responseCode) {
    if (ResponseCode.SUCC != responseCode) {
      handleOnRetry(delayTask);
      return true;
    }
    return false;
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
