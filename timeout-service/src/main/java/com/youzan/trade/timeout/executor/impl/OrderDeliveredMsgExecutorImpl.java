package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.LockIdConstants;
import com.youzan.trade.timeout.executor.AbstractExecutor;
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
 * 订单发货后变成交易完成发送消息的延时任务
 *
 * @author apple created at: 15/12/29 下午11:08
 */
@Component
public class OrderDeliveredMsgExecutorImpl extends AbstractExecutor {

  @Resource(name = "orderDeliveredMsgTaskFetcher")
  private TaskFetcher taskFetcher;

  @Resource(name = "orderDeliveredMsgTaskHandlerImpl")
  private TaskHandler taskHandler;

  @Scheduled(cron = "${order.delivered.msg.task.cron}")
  public void start() {
    execute(LockIdConstants.ORDER_DELIVERED_MSG_EXECUTOR_LOCK_ID, taskHandler);
  }

  @Override
  protected List<DelayTask> getTaskList() {
    return taskFetcher.fetch();
  }
}
