package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/28 上午10:54
 */
@AllArgsConstructor
public enum SafeType {

  REFUND_ONLY(1, "我要退款，但不退货"),
  REFUND_RETURN(2, "我要退款，并退货");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
