package com.youzan.trade.timeout.executor;

import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskLockService;
import com.youzan.trade.util.LogUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author apple created at: 15/12/28 下午6:02
 */
@Slf4j
public abstract class AbstractExecutor implements Executor {

  @Resource
  private DelayTaskLockService delayTaskLockService;

  @Resource(name = "taskExecutor")
  private ThreadPoolTaskExecutor taskExecutor;

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
    monitorThreadPool();
    List<DelayTask> delayTaskList = getTaskList();

    if (!CollectionUtils.isEmpty(delayTaskList)) {
      delayTaskList.forEach(delayTask -> taskHandler.handle(delayTask));
    }
  }

  private void monitorThreadPool() {
    LogUtils.info(log, "线程池的queue实时大小: {}",
                  taskExecutor.getThreadPoolExecutor().getQueue().size());
  }

  protected abstract List<DelayTask> getTaskList();

}
