package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.timeout.handler.AbstractMsgTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.TaskResult;
import com.youzan.trade.util.LogUtils;

import com.google.common.collect.Maps;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;


/**
 * @author apple created at: 15/10/30 下午7:40
 */
@Slf4j
@Component("safeMsgTaskHandlerImpl")
public class SafeMsgTaskHandlerImpl extends AbstractMsgTaskHandler {

  private String callPath = "trade.safe.timeout.sendMsg";

  @Async("defaultThreadPoolTaskExecutor")
  @Override
  public void handle(DelayTask delayTask) {
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
    }
  }

  private void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params) {
    params.put("safe_no", delayTask.getBizId());
    params.put("state", delayTask.getBizState());
  }

  @Override
  protected String getCallPath() {
    return this.callPath;
  }
}
