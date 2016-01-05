package com.youzan.trade.timeout.model;

import java.util.Date;

import lombok.Data;

/**
 * @author hupp created at: 15/12/28 下午6:01
 */
@Data
public class OrderSuccessLog {

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

  /**
   * 中断持续时间
   */
  Integer suspendedPeriod;
}
