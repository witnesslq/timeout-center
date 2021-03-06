package com.youzan.trade.timeout.dal.dataobject;

import lombok.Data;

/**
 *
 * 订单对应DO。
 *
 * 由于多维度买家卖家表表结构相同，所以此DO对应的多个数据库表:
 *   1. buyer_order
 *   2. seller_order
 *
 * @author wangxiaolei
 */
@Data
public class OrderDO {
  /** auto increment id --- primary key */
  private Integer id;
  /** 订单号 根据规则拼接而成, 所以为string类型 --- unique key */
  private String orderNo;
  /** 店铺id*/
  private Integer kdtId;
  /** 卖家ID */
  private Integer customerId;
  /** 卖家名称 */
  private String customerName;
  /** 卖家类型 */
  private Integer customerType;
  /** 买家ID */
  private Integer buyerId;
  /** 买家手机号 */
  private String buyerPhone;
  /** 访客hash */
  private String tourist;
  /** 订单状态 对应db字段 --> state */
  private Integer state;
  /** 库存状态 0.未扣库存 1.已扣库存 */
  private Integer stockState;
  /** 支付状态 */
  private Integer payState;
  /** 物流状态 */
  private Integer expressState;
  /** 维权状态 */
  private Integer feedback;
  /** 退款状态 */
  private Integer refundState;
  /** 关闭状态 0.未关闭 1.已关闭*/
  private Integer closeState;
  /** 订单类型 0.正常订单 1.送礼单 2.代付单 */
  private Integer orderType;
  /** 配送方式 0.快递配送 1.到店自提 */
  private Integer expressType;
  /** 购买方式 1.weixin */
  private Integer buyWay;
  /** 异常订单 0.正常 1.异常 */
  private Integer normal;
  /** 是否拆分了子订单 */
  private Integer hasChild;
  /** 订单更新时间 */
  private Integer updateTime;
  /** 预定时间 */
  private Integer bookTime;
  /** 自动过期时间 */
  private Integer expireTime;
  /** 支付时间 */
  private Integer payTime;
  /** 发货时间 */
  private Integer expressTime;
  /** 维权时间 */
  private Integer feedbackTime;
  /** 退款时间 */
  private Integer refundTime;
  /** 交易成功时间 */
  private Integer successTime;
  /** 订单关闭时间 */
  private Integer closeTime;
  /** 订单对应的货币种类 */
  private Integer currency;
}
