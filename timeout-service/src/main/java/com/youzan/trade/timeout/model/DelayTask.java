package com.youzan.trade.timeout.model;

import lombok.Data;

/**
 * @author apple created at: 15/10/22 下午6:01
 */
@Data
public class DelayTask {

  private Integer bizType;

  private Integer bizId;

  private Byte status;

  private Byte closeReason;

  private Integer delayStartTime;

  private Integer delayEndTime;

  private Integer delayTimes;

  private String delayReason;

  private Integer createTime;

  private Integer updateTime;
}
