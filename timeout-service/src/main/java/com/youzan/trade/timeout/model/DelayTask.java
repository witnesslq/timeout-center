package com.youzan.trade.timeout.model;

import lombok.Data;

/**
 * @author apple created at: 15/10/22 下午6:01
 */
@Data
public class DelayTask {
  private Integer bizType;

  private Integer bizId;

  private Integer status;

  private Integer closeReason;

  private Integer runTime;

  private Integer delayTimes;

  private Integer createTime;

  private Integer updateTime;

  private String delayReason;
}
