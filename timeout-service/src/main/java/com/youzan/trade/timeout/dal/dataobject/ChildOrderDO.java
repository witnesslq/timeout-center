package com.youzan.trade.timeout.dal.dataobject;

import lombok.Data;


/**
 * 子订单
 * <p>
 * 对应数据库表: child_order
 *
 * @author: chenhuajiang@youzan.com
 * create date:: 15/8/29
 */
@Data
public class ChildOrderDO {
  private Integer id;

  private String orderNo;

  private Integer kdtId;

  private Integer customerId;

  private String customerName;

  private Integer state;

  private Integer feedback;

  private Integer createdTime;

  private Integer updateTime;

  private Integer payTime;

  private Integer pay;

  private Integer postage;

  private Integer realPay;

  private Integer saveMoney;

  private String remark;

  private Integer star;

  private String outerTransactionNumber;

  private Integer bookTime;

  private Integer cancelTime;

  private Integer expireTime;

  private Integer buyWay;

  private Integer expressId;

  private String expressNo;

  private Integer expressTime;

  private Integer normal;

  private Integer feedbackTime;

  private Integer buyerId;

  private String buyerPhone;

  private Integer startSoldTime;

  private Integer closeState;

  private Integer signTime;

  private Integer closeTime;

  private String source;

  private String track;

  private String tourist;

  private Integer expressType;

  private Integer customerType;

  private Integer realPostage;

  private Integer stockState;

  private String parentOrderNo;
}
