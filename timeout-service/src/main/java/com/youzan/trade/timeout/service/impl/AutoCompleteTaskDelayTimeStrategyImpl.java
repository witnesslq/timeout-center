package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.OrderType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
@Component(value = "autoCompleteTaskDelayTimeStrategyImpl")
public class AutoCompleteTaskDelayTimeStrategyImpl extends AbstractOrderRelatedDelayTimeStrategy {

  @Value("${delay.time.autocomplete.normal.initial}")
  private int normalOrderInitDelayTime;

  @Value("${delay.time.autocomplete.pf.initial}")
  private int pfOrderInitDelayTime;

  @Value("${delay.time.increment.first}")
  private int firstIncrement;

  @Value("${delay.time.increment.second}")
  private int secondIncrement;

  @Value("${delay.time.increment.third}")
  private int thirdIncrement;

  @Value("${delay.time.increment.default}")
  private int defaultIncrement;

  @Override
  public int getInitialDelayTimeByOrderType(int bizType, int orderType) {
    if(orderType== OrderType.PF.type()){
      return pfOrderInitDelayTime;
    }else {
      return normalOrderInitDelayTime;
    }
  }

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
    return 0;
  }
}
