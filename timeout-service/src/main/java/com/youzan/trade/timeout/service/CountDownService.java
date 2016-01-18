package com.youzan.trade.timeout.service;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/18 .
 */
public interface CountDownService {

  /**
   * 判断该订单是否需要发送通知.
   * 如果出现异常，则默认不发送短信
   * @return boolean true:发送短信;false:不发送短信
   */
  boolean shouldSendMsg(String orderNo, Integer kdtId);

}
