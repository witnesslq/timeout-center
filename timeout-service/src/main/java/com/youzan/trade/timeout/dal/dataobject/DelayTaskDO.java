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

  private Date delayStartTime;

  private Date delayEndTime;

  private Integer delayTimes;

  private Integer msgStatus;

  private Date msgEndTime;

  private Date createTime;

  private Date updateTime;

  private Date suspendTime;
}
