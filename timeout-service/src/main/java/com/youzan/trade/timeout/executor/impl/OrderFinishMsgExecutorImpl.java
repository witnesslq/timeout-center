package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 订单发货后变成交易完成发送消息的延时任务
 *
 * @author apple created at: 15/12/29 下午11:08
 */
@Component
public class OrderFinishMsgExecutorImpl extends AbstractExecutor {

  @Override
  protected List<DelayTask> getTaskList() {
    return null;
  }
}
