package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.executor.Executor;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/26 上午9:22
 */
@Component("executorImpl")
public class ExecutorImpl implements Executor {

  @Resource
  private DelayTaskService delayTaskService;

  @Resource
  private TaskHandler taskHandler;

  @Scheduled(cron = "0 0 * * * ?")
  @Override
  public void execute() {
    System.out.println("execute");

    List<DelayTask> delayTaskList = delayTaskService.getListWithTimeoutCurrently();

    if (CollectionUtils.isEmpty(delayTaskList)) {
      return ;
    }

    delayTaskList.forEach(delayTask -> taskHandler.handle(delayTask));

    System.out.println(delayTaskList.size());
  }
}
