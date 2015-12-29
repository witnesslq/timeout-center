package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单发货后变成交易完成的延时任务
 *
 * @author apple created at: 15/12/29 下午11:02
 */
@Component
public class OrderFinishExecutorImpl extends AbstractExecutor {

  @Value("${order.finish.scan.once.max.size}")
  private int maxSize;

  @Override
  protected List<DelayTask> getTaskList() {
    return null;
  }
}
