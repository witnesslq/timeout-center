package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/11/6 下午11:56
 */
@AllArgsConstructor
public enum DelayState {

  NEEDED(1, "需要超时"),
  NOTNEEDED(0, "不需要超时");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
