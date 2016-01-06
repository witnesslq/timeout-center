package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.fetcher.TaskFetcher;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

/**
 * 维权业务发送消息的超时任务
 *
 * @author apple created at: 15/10/30 下午7:39
 */
@Component
public class SafeMsgExecutorImpl extends AbstractExecutor {

  @Resource(name = "safeMsgTaskHandlerImpl")
  private TaskHandler taskHandler;

  @Resource(name = "safeMsgTaskFetcher")
  private TaskFetcher taskFetcher;

  @Scheduled(cron = "${safe.msg.task.cron}")
  public void start() {
    execute(LockIdConstants.SAFE_MSG_EXECUTOR_LOCK_ID, taskHandler);
  }

  @Override
  protected List<DelayTask> getTaskList() {
    return taskFetcher.fetch();
  }


}
