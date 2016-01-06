package com.youzan.trade.timeout.fetcher.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.fetcher.AbstractTaskFetcher;
import com.youzan.trade.timeout.model.DelayTask;

import java.util.List;

/**
 * @author apple created at: 16/1/5 下午9:59
 */
public class SimpleMsgTaskFetcher extends AbstractTaskFetcher {

  @Override
  public List<DelayTask> fetch() {
    return delayTaskService.getListWithBizTypeAndMsgTimeoutCurrently(bizType.code(), maxSize);
  }
}
