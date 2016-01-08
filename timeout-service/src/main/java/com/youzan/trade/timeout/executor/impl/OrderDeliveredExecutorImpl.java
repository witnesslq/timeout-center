package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.executor.AbstractExecutor;
import com.youzan.trade.timeout.fetcher.TaskFetcher;
import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

/**
 * 订单发货后变成交易完成的延时任务
 *
 * @author apple created at: 15/12/29 下午11:02
 */
@Component
public class OrderDeliveredExecutorImpl extends AbstractExecutor {

  @Resource(name = "orderDeliveredTaskHandlerImpl")
  private TaskHandler taskHandler;

  @Resource(name = "orderDeliveredTaskFetcher")
  private TaskFetcher taskFetcher;

  @Scheduled(cron = "${order.delivered.task.cron}")
  public void start() {
    execute(LockIdConstants.ORDER_DELIVERED_EXECUTOR_LOCK_ID, taskHandler);
  }

  @Override
  protected List<DelayTask> getTaskList() {
    return taskFetcher.fetch();
  }
}
