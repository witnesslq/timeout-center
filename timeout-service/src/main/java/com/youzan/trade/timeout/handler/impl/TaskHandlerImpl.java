package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.timeout.handler.TaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/23 下午8:20
 */
@Component
public class TaskHandlerImpl implements TaskHandler {

  @Resource
  private DelayTaskService delayTaskService;

  @Override
  public void handle(DelayTask delayTask) {
    /**
     * 根据delayTask,判断如何进行外部调用
     * 1. 调用成功, 则回写delay_task的status = 2, close_reason = ?, delay_times加1, update_time
     *
     * 2. 调用失败,则回写delay_task, 令其delay_times加1, update_time, 更新delay_end_time(不同的策略)
     */
  }
}
