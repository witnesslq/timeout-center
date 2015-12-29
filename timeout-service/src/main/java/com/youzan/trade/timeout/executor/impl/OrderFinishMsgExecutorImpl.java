package com.youzan.trade.timeout.executor.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.Resource;

/**
 * 订单发货后变成交易完成发送消息的延时任务
 *
 * @author apple created at: 15/12/29 下午11:08
 */
@Component
public class OrderFinishMsgExecutorImpl extends AbstractExecutor {

  @Value("${order.finish.msg.scan.once.max.size}")
  private int maxSize;

  @Resource
  private DelayTaskService delayTaskService;

  @Override
  protected List<DelayTask> getTaskList() {
    return delayTaskService.getListWithBizTypeAndMsgTimeoutCurrently(BizType.OrderDelivered.code(), maxSize);
  }
}
