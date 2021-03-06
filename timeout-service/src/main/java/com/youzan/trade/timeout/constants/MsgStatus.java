package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/29 下午10:44
 */
@AllArgsConstructor
public enum MsgStatus {

  NONE(0, "没有消息任务"),
  ACTIVE(10, "活跃"),
  CLOSED(20, "已关闭");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
