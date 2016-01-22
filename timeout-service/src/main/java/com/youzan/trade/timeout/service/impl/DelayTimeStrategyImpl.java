package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.service.AbstractDelayTimeStrategy;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.util.TimeUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author apple created at: 15/10/26 下午7:45
 */
@Component("delayTimeStrategyImpl")
public class DelayTimeStrategyImpl extends AbstractDelayTimeStrategy {

  @Value("${delay.time.initial}")
  private int initialDelayTime;

  @Value("${delay.time.initial.spring.2016}")
  private int initialDelayTimeForSpring2016;

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState, int delayStartTime) {
    if (TimeUtils.isInSpring2016(delayStartTime)) {
      return initialDelayTimeForSpring2016;
    }

    return initialDelayTime;
  }

}
