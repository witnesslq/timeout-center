package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

/**
 * 维权业务的超时任务
 * 
 * @author apple created at: 15/10/26 上午9:22
 */
@Component
public class SafeExecutorImpl extends AbstractExecutor {

  private static final int LOCK_ID = LockIdConstants.SAFE_EXECUTOR_LOCK_ID;

  private final int maxSize = 100;

  @Resource
  private DelayTaskService delayTaskService;

  @Resource(name = "safeTaskHandlerImpl")
  private TaskHandler taskHandler;

  @Scheduled(cron = "${safe.task.cron}")
  public void start() {
    execute(LOCK_ID, taskHandler);
  }

  @Override
  protected List<DelayTask> getTaskList() {
    return delayTaskService.getListWithBizTypeAndTimeoutCurrently(BizType.SAFE.code(), maxSize);
  }
}
