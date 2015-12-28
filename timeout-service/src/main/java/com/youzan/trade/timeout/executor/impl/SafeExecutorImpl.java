package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskLockService;
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
@Component("executorImpl")
public class SafeExecutorImpl extends AbstractExecutor {

  private static final int LOCK_ID = LockIdConstants.SAFE_EXECUTOR_LOCK_ID;

  private final int maxSize = 100;

  @Resource
  private DelayTaskService delayTaskService;

  @Resource
  private DelayTaskLockService delayTaskLockService;

  @Resource(name = "safeTaskHandlerImpl")
  private TaskHandler taskHandler;

  // 每分钟启动一次
  @Scheduled(cron = "${safe.task.cron}")
  public void start() {
    execute(LOCK_ID);
  }

  @Override
  public void execute(int lockId) {
    /**
     * 先尝试获取锁
     * 如果获取不到,则什么都不做
     */
    if (!delayTaskLockService.lockByLockId(lockId)) {
      return;
    }

    try {
      doExecute();
    } finally {
      /**
       * 最后释放锁
       */
      delayTaskLockService.unlockByLockId(lockId);
    }
  }

  private void doExecute() {
    List<DelayTask> delayTaskList = delayTaskService.getListWithBizTypeAndTimeoutCurrently(
        BizType.SAFE.code(), maxSize);

    if (!CollectionUtils.isEmpty(delayTaskList)) {
      delayTaskList.forEach(delayTask -> taskHandler.handle(delayTask));
    }
  }
}
