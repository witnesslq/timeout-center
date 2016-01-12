package com.youzan.trade.timeout.service;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
public  abstract  class AbstractOrderRelatedDelayTimeStrategy implements DelayTimeStrategy{

  public abstract int getInitialDelayTimeByOrderType(int bizType,int orderType);
}
