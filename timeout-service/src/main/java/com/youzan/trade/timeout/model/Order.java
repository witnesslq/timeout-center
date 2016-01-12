package com.youzan.trade.timeout.model;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Data
public class Order {
  private static final long serialVersionUID = 7552338130865273545L;

  private Integer id;

  /** 订单号 根据规则拼接而成, 所以为string类型 --- unique key */
  @JSONField(name = "order_no")
  private String orderNo;

  /** 店铺id*/
  @JSONField(name = "kdt_id")
  private Integer kdtId;

  /** 卖家ID */
  @JSONField(name = "customer_id")
  private Integer customerId;

  /** 卖家名称 */
  @JSONField(name = "customer_name")
  private String customerName;

  /** 卖家类型 */
  @JSONField(name = "customer_type")
  private Integer customerType;

  /** 买家ID */
  @JSONField(name = "buyer_id")
  private Integer buyerId;

  /** 买家手机号 */
  @JSONField(name = "buyer_phone")
  private String buyerPhone;
  /** 访客hash */
  private String tourist;

  /** 订单状态 对应db字段 -- state */
  @JSONField(name = "state")
  private Integer orderState;

  /** 库存状态 0.未扣库存 1.已扣库存 */
  @JSONField(name = "stock_state")
  private Integer stockState;

  /** 支付状态 */
  @JSONField(name = "pay_state")
  private Integer payState;

  /** 物流状态 */
  @JSONField(name = "express_state")
  private Integer expressState;
  /** 维权状态 */
  private Integer feedback;

  /** 退款状态 */
  @JSONField(name = "refund_state")
  private Integer refundState;

  /** 关闭状态 0.未关闭 1.已关闭*/
  @JSONField(name = "close_state")
  private Integer closeState;

  /** 订单类型 0.正常订单 1.送礼单 2.代付单 */
  @JSONField(name = "order_type")
  private Integer orderType;

  /** 配送方式 0.快递配送 1.到店自提 */
  @JSONField(name = "express_type")
  private Integer expressType;

  /** 购买方式 1.weixin */
  @JSONField(name = "buy_way")
  private Integer buyWay;
  /** 异常订单 0.正常 1.异常 2.超付 */
  private Integer normal;

  /** 是否拆分了子订单 */
  @JSONField(name = "has_child")
  private Integer hasChild;

  /** 订单更新时间 */
  @JSONField(name = "update_time")
  private Integer updateTime;

  /** 预定时间 */
  @JSONField(name = "book_time")
  private Integer bookTime;

  /** 自动过期时间 */
  @JSONField(name = "expire_time")
  private Integer expireTime;

  /** 支付时间 */
  @JSONField(name = "pay_time")
  private Integer payTime;

  /** 发货时间 */
  @JSONField(name = "express_time")
  private Integer expressTime;

  /** 维权时间 */
  @JSONField(name = "feedback_time")
  private Integer feedbackTime;

  /** 退款时间 */
  @JSONField(name = "refund_time")
  private Integer refundTime;

  /** 交易成功时间 */
  @JSONField(name = "success_time")
  private Integer successTime;

  /** 订单关闭时间 */
  @JSONField(name = "close_time")
  private Integer closeTime;
  /** 订单对应的货币种类 */
  private Integer currency;

  /** ump红包 */
  private String luckyMoneyAlias;
  /** ump红包token */
  private String luckyMoneyToken;
  /** ump优惠 */
  private String couponId;
  /** ump优惠类型 */
  private String couponType;
  /** 是否虚拟订单 */
  private Integer isVirtual;

  /** 是否禁用ump相关*/
  private boolean forbidUmp;
}
