package com.youzan.trade.timeout.dal.dataobject;

import java.util.Date;

import lombok.Data;

/**
 * @author apple created at: 15/10/22 下午6:04
 */
@Data
public class DelayTaskDO {
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
