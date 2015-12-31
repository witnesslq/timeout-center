package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/27 下午8:51
 */
@AllArgsConstructor
public enum SafeState {

  BUYER_START(201, "买家发起, 等待商家处理退款申请"),
  BUYER_RESTART(202, "被拒绝后再次发起, 等待商家处理退款申请"),
  SELLER_REJECTED(203, "卖家拒绝, 等待买家处理"),
  INVOLVED(204, "已经申请有赞客满介入"),
  SELLER_ACCEPTED(205, "卖家接受"),
  BUYER_SENT(206, "买家已退货，等待商家确认收货"),
  SELLER_NOT_RECEIVED(207, "商家未收到货，不同意退款申请"),
  CLOSED(249, "退款关闭"),
  FINISHED(250, "退款结束");


  private int code;
  private String description;

  public int code() {
    return this.code;
  }

  /**
   * 根据维权状态获取一个枚举
   *
   * @param safeStateCode 维权状态
   * @return 枚举
   */
  public static SafeState getSafeStateByCode(int safeStateCode) {
    for (SafeState entry : values()) {
      if (entry.code == safeStateCode) {
        return entry;
      }
    }
    return null;
  }
}
