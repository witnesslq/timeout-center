package com.youzan.trade.timeout.fetcher;

import com.youzan.trade.timeout.model.DelayTask;

import java.util.List;

/**
 * @author apple created at: 16/1/5 下午7:32
 */
public interface TaskFetcher {

  /**
   * 获取待处理的任务列表
   *
   * @return 待处理的任务列表
   */
  List<DelayTask> fetch();
}
