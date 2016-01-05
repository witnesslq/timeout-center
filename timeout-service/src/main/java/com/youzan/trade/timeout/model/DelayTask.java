package com.youzan.trade.timeout.model;

import java.util.Date;

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

  private Integer bizShardKey;

  private Integer status;

  private Integer closeReason;

  private Date delayStartTime;

  private Date delayEndTime;

  private Date suspendTime;

  private Integer delayTimes;

  private Integer msgStatus;

  private Date msgEndTime;

  private Date createTime;

  private Date updateTime;
}
