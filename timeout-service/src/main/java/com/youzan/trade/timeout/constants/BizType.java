package com.youzan.trade.timeout.constants;

import lombok.AllArgsConstructor;

/**
 * @author apple created at: 15/10/26 上午10:36
 */
@AllArgsConstructor
public enum BizType {

  SAFE(10, "维权");

  private int code;
  private String description;
}
