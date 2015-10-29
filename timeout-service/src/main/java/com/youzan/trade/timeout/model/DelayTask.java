package com.youzan.trade.timeout.model;

import lombok.Data;

/**
 * @author apple created at: 15/10/22 下午6:01
 */
@Data
public class DelayTask {

  /**
   * 数据库主键
   * 目前没有唯一键,更新操作时用主键作唯一标识
   */
  private Integer id;

  private Integer bizType;

  private String bizId;

  private Integer bizState;

  private Integer status;

  private Integer closeReason;

  private Integer delayStartTime;

  private Integer delayEndTime;

  private Integer delayTimes;

  private Integer createTime;

  private Integer updateTime;
}
