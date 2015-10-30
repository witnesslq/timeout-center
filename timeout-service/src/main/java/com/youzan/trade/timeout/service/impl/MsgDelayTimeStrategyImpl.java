package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.TimeConstants;
import com.youzan.trade.timeout.service.DelayTimeStrategy;

import org.springframework.stereotype.Component;

/**
 * @author apple created at: 15/10/29 下午11:06
 */
@Component("msgDelayTimeStrategyImpl")
public class MsgDelayTimeStrategyImpl implements DelayTimeStrategy {

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    return TimeConstants.ONE_DAY_IN_SECONDS;
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState) {
    return 3 * TimeConstants.ONE_DAY_IN_SECONDS;
  }
}
