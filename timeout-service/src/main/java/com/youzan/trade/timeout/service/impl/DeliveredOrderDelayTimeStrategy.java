package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.service.DelayTimeStrategy;

import org.springframework.stereotype.Component;

import lombok.Data;


/**
 * @author apple created at: 15/10/26 下午7:45
 */
@Component("delayTimeStrategyImpl")
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
    return -1;
  }

}
