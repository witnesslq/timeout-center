package com.youzan.trade.timeout.service;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author apple created at: 16/1/22 下午5:13
 */
public abstract class AbstractDelayTimeStrategy implements DelayTimeStrategy {

  @Value("${delay.time.increment.first}")
  protected int firstIncrement;

  @Value("${delay.time.increment.second}")
  protected int secondIncrement;

  @Value("${delay.time.increment.third}")
  protected int thirdIncrement;

  @Value("${delay.time.increment.default}")
  protected int defaultIncrement;

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    switch (delayTimes) {
      case 0 : return firstIncrement;

      case 1 : return secondIncrement;

      case 2 : return thirdIncrement;

      default: return defaultIncrement;
    }
  }
}
