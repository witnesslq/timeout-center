package com.youzan.trade.timeout.fetcher;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.service.DelayTaskService;

import javax.annotation.Resource;

import lombok.Setter;

/**
 * @author apple created at: 16/1/5 下午8:09
 */
public abstract class AbstractTaskFetcher implements TaskFetcher {

  @Setter
  protected int maxSize;

  @Setter
  protected BizType bizType;

  @Resource
  protected DelayTaskService delayTaskService;

}
