package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.TimeConstants;
import com.youzan.trade.timeout.service.DelayTimeStrategy;

import org.springframework.stereotype.Component;


/**
 * @author apple created at: 15/10/26 下午7:45
 */
@Component("delayTimeStrategyImpl")
public class DelayTimeStrategyImpl implements DelayTimeStrategy {

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    // todo : checkArgument
    switch (delayTimes) {
      case 0 : return 5 * TimeConstants.ONE_MINUTE_IN_SECONDS;

      case 1 : return TimeConstants.ONE_HOUR_IN_SECONDS;

      case 2 : return 6 * TimeConstants.ONE_HOUR_IN_SECONDS;

      default: return TimeConstants.ONE_DAY_IN_SECONDS;
    }
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState) {
    return 7 * TimeConstants.ONE_DAY_IN_SECONDS;
  }

}
