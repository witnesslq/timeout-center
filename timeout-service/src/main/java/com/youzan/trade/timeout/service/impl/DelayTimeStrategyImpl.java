package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.TimeConstants;
import com.youzan.trade.timeout.service.DelayTimeStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author apple created at: 15/10/26 下午7:45
 */
@Component("delayTimeStrategyImpl")
public class DelayTimeStrategyImpl implements DelayTimeStrategy {

  @Value("${delay.time.increment.first}")
  private int firstIncrement;

  @Value("${delay.time.increment.second}")
  private int secondIncrement;

  @Value("${delay.time.increment.third}")
  private int thirdIncrement;

  @Value("${delay.time.increment.default}")
  private int defaultIncrement;

  @Value("${delay.time.initial}")
  private int initialDelayTime;

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    // todo : checkArgument
    switch (delayTimes) {
      case 0 : return firstIncrement;

      case 1 : return secondIncrement;

      case 2 : return thirdIncrement;

      default: return defaultIncrement;
    }
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState) {
    return initialDelayTime;
  }

}
