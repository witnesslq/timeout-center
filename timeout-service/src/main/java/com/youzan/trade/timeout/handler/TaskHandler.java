package com.youzan.trade.timeout.handler;

import com.youzan.trade.timeout.model.DelayTask;

/**
 * @author apple created at: 15/10/23 下午8:19
 */
public interface TaskHandler {

  /**
   * 处理一个延时任务
   *
   * @param delayTask
   */
  void handle(DelayTask delayTask);
}
