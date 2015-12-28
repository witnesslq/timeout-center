package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.executor.Executor;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskLockService;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/12/28 下午6:02
 */
public abstract class AbstractExecutor implements Executor {

  @Resource
  private DelayTaskLockService delayTaskLockService;

  @Override
  public void execute(int lockId, TaskHandler taskHandler) {
    /**
     * 先尝试获取锁
     * 如果获取不到,则什么都不做
     */
    if (!delayTaskLockService.lockByLockId(lockId)) {
      return;
    }

    try {
      doExecute(taskHandler);
    } finally {
      /**
       * 最后释放锁
       */
      delayTaskLockService.unlockByLockId(lockId);
    }
  }

  private void doExecute(TaskHandler taskHandler) {
    List<DelayTask> delayTaskList = getTaskList();

    if (!CollectionUtils.isEmpty(delayTaskList)) {
      delayTaskList.forEach(delayTask -> taskHandler.handle(delayTask));
    }
  }

  protected abstract List<DelayTask> getTaskList();
}
