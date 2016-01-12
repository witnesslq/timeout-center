package com.youzan.trade.timeout.fetcher.impl;

import com.youzan.trade.timeout.fetcher.AbstractTaskFetcher;
import com.youzan.trade.timeout.model.DelayTask;

import java.util.List;

/**
 * @author apple created at: 16/1/5 下午8:14
 */
public class SimpleTaskFetcher extends AbstractTaskFetcher {

  @Override
  public List<DelayTask> fetch() {
    return delayTaskService.getListWithBizTypeAndTimeoutCurrently(bizType.code(), maxSize);
  }
}
