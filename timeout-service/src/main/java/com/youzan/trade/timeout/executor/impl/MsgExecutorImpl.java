package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.executor.Executor;
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
 * @author apple created at: 15/10/30 下午7:39
 */
@Component("msgExecutorImpl")
public class MsgExecutorImpl implements Executor {

  private static final int LOCK_ID = LockIdConstants.SAFE_MSG_EXECUTOR_LOCK_ID;

  @Resource
  private DelayTaskService delayTaskService;

  @Resource
  private DelayTaskLockService delayTaskLockService;

  @Resource(name = "safeMsgTaskHandlerImpl")
  private TaskHandler safeMsgTaskHandlerImpl;

  // 每小时启动一次
  @Scheduled(cron = "0 0 * * * ?")
  @Override
  public void execute() {
    /**
     * 先尝试获取锁
     * 如果获取不到,则什么都不做
     */
    if (!delayTaskLockService.lockByLockId(LOCK_ID)) {
      return;
    }

    List<DelayTask> delayTaskList = delayTaskService.getListWithMsgTimeoutCurrently();

    if (CollectionUtils.isEmpty(delayTaskList)) {
      return ;
    }

    delayTaskList.forEach(delayTask -> {
      if (delayTask.getBizType() == BizType.SAFE.code()) {
        safeMsgTaskHandlerImpl.handle(delayTask);
      }
    });

    delayTaskLockService.unlockByLockId(LOCK_ID);
  }
}
