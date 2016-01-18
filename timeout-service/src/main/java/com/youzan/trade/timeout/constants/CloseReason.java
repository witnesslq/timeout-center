package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/26 上午10:39
 */
@AllArgsConstructor
public enum CloseReason {

  /**
   * 命名不太合适
   * 如有更好,请修改
   */
  NOT_CLOSED(0, "不关闭"),
  AHEAD(10, "操作在超时到期前已提前完成"),
  SUCCESS(20, "操作超时,并执行成功"),
  FAILURE_NO_RETRY(21, "操作超时,执行失败,但不再重试"),
  MANUAL_SUCCESS(22,"手工完成");

  private int code;
  private String description;

  public int code() {
    return this.code;
  }
}
