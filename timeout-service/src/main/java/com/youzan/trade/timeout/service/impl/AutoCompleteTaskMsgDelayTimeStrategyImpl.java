package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.OrderType;
import com.youzan.trade.timeout.service.AbstractOrderRelatedDelayTimeStrategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
@Component(value = "autoCompleteTaskMsgDelayTaskTimeStrategyImpl")
public class AutoCompleteTaskMsgDelayTimeStrategyImpl
    extends AbstractOrderRelatedDelayTimeStrategy {

  @Value("${delay.msg.time.autocomplete.normal.initial}")
  private int normalOrderInitDelayTime;

  @Value("${delay.msg.time.autocomplete.pf.initial}")
  private int pfOrderInitDelayTime;

  @Value("${delay.msg.time.increment}")
  private int delayTimeIncrement;

  /**
   *
   * @param bizType
   * @param orderType
   * @return
   */
  @Override
  public int getInitialDelayTimeByOrderType(int bizType, int orderType) {
    if (orderType == OrderType.PF.type()) {
      return pfOrderInitDelayTime;
    } else if (orderType == OrderType.FX_CAIGOUDAN.type()) {
      //采购单不下饭短信
      return -1;
    } else {
      return normalOrderInitDelayTime;
    }
  }

  @Override
  public int getNextDelayIncrement(int delayTimes) {
    return delayTimeIncrement;
  }

  @Override
  public int getInitialDelayTime(int bizType, String bizId, int bizState) {
    return 0;
  }
}
