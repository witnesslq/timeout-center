package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.service.DelayTimeStrategy;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
public  abstract  class AbstractOrderRelatedDelayTimeStrategy implements DelayTimeStrategy{

  public abstract int getInitialDelayTimeByOrderType(int bizType,int orderType);
}
