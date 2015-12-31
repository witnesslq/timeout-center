package com.youzan.trade.timeout.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 交易订单状态。
 *
 *
 * @author wangxiaolei
 */
@AllArgsConstructor
@Getter
public enum OrderState {
  BOOK(1, "创建"),
  ADDRESS(2, "填收货地址"),
  TOPAY(3, "待付款"),
  //    PAYING(4, "待系统确认"),
  PAID(5, "待发货"),
  SENT(6, "已发货"),
  CLOSE(99, "关闭"),
  SUCCESS(100, "完成");

  /** 该状态在数据库中对应的state value */
  private Integer state;
  /** 状态描述 */
  private String desc;

  /**
   *
   * 通过event name 获取其对应的order state value.
   *
   * @param name 状态名称
   * @return 状态值
   */
  public static Integer getStateByName(String name) {
    if(name == null) return null;
    for (OrderState eventType : values()) {
      if(name.equals(eventType.name())) {
        return eventType.getState();
      }
    }
    return null;
  }

  /**
   *
   * 通过订单状态获取其对应的event
   *
   * @param orderState 状态值
   * @return 状态枚举
   */
  public static OrderState getEventTypeByOrderState(Integer orderState) {
    if(orderState == null) return null;
    for (OrderState eventType : values()) {
      if(eventType.getState().equals(orderState)) {
        return eventType;
      }
    }
    return null;
  }
}
