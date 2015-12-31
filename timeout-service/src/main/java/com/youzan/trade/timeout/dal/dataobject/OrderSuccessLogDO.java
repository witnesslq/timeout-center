package com.youzan.trade.timeout.dal.dataobject;

import lombok.Data;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Data
public class OrderSuccessLogDO {

  /**
   * 主键Id
   */
  Integer id;

  /**
   * 订单号
   */
  String orderNo;

  /**
   * 店铺ID
   */
  Integer kdtId;

  /**
   * 维权创建时间
   */
  Integer createTime;

  /**
   * 维权结束时间
   */
  Integer finishTime;

  /**
   * 剩余时间
   */
  Integer remainTime;
}
