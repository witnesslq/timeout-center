package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.model.DelayTask;

import java.util.List;

/**
 * @author apple created at: 15/10/23 上午11:53
 */
public interface DelayTaskService {

  /**
   * 获取某个时间点已经超时的任务
   *
   * @return
   */
  List<DelayTask> getListWithTimeout(int timePoint);

  /**
   * 获取当前已经超时的任务
   *
   * @return
   */
  List<DelayTask> getListWithTimeoutCurrently();
}
