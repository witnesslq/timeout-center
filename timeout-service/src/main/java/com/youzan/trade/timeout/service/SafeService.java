package com.youzan.trade.timeout.service;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
public interface SafeService {

  /**
   * 检查维权记录对应订单是否处于完成状态
   * @param orderNo
   * @param kdtId
   * @return
   */
  Boolean checkOrderFeedbackFinish(String orderNo,Integer kdtId);

}
