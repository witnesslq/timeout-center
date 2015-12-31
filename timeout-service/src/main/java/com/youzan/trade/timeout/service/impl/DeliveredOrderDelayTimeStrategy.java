package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.TimeConstants;
import com.youzan.trade.timeout.service.DelayTimeStrategy;


import lombok.Data;


/**
 * @author liwenjia created at: 15/12/30
 */
@Data
public class DeliveredOrderDelayTimeStrategy implements DelayTimeStrategy {

  private int autoCompleteTime;

  private int notifyTime;

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    return -1;
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState) {
    return TimeConstants.ONE_DAY_IN_SECONDS * autoCompleteTime;
  }

}
