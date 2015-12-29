package com.youzan.trade.timeout.constants;

import com.youzan.trade.timeout.executor.impl.OrderFinishExecutorImpl;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/26 上午10:36
 */
@AllArgsConstructor
public enum BizType {

  SAFE(10, "维权"),
  OrderDelivered(26, "订单已发货");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
