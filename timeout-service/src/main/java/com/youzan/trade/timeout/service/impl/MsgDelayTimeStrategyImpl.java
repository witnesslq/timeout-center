package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.service.DelayTimeStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author apple created at: 15/10/29 下午11:06
 */
@Component("msgDelayTimeStrategyImpl")
public class MsgDelayTimeStrategyImpl implements DelayTimeStrategy {

  @Value("${delay.msg.time.increment}")
  private int delayTimeIncrement;

  @Value("${delay.msg.time.initial}")
  private int initialDelayTime;

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    return delayTimeIncrement;
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState, int delayStartTime) {
    return initialDelayTime;
  }
}
