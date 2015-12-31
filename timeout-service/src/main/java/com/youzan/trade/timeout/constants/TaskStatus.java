package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/26 上午9:56
 */
@AllArgsConstructor
public enum TaskStatus {
  ACTIVE(10, "活跃"), // 活跃/恢复
  SUSPENDED(15,"中断"),
  CLOSED(20, "已关闭");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
