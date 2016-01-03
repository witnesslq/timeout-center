package com.youzan.trade.timeout.constants;


import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * 订单类型枚举
 *
 * Created by xiongpeng on 15/9/17.
 */
public enum OrderType {

  NORMAL(0, "普通订单"),
  GIFT(1, "我要送人"),
  PEERPAY(2, "代付"),
  FX_CAIGOUDAN(3, "分销采购单"),
  PRESENT(4, "赠品"),
  WISH(5, "心愿单"),
  QRCODE(6, "二维码订单"),
  FX_MERGED(7, "合并付货款"),
  VERIFIED(8, "1分钱实名认证"),
  PINJIAN(9, "品鉴"),
  REBATE(15, "返利"),
  FX_QUANYUANDIAN(51, "全员开店"),
  FX_DEPOSIT(52, "保证金");

  OrderType(int type, String desc) {
    this.type = type;
    this.desc = desc;
  }

  private static final Map<Integer, OrderType> orderTypeMap = new HashMap<>();
  static {
    for(OrderType orderType : OrderType.values()){
      orderTypeMap.put(orderType.getType(), orderType);
    }
  }


  @Getter
  private int type;
  @Getter
  private String desc;

  public int type() {
    return this.type;
  }

  public String desc() {
    return this.desc;
  }

  public static OrderType getByTypeValue(int orderType) {
    return orderTypeMap.get(orderType);
  }

  /*
  public static OrderType getByTypeValue(int orderType) {
    for (OrderType type : values()) {
      if(type.type() == orderType) {
        return type;
      }
    }
    return null;
  }
  */
}
