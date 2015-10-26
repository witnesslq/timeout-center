package com.youzan.trade.timeout.transfer;

import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author apple created at: 15/10/23 上午11:42
 */
public class DelayTaskDataTransfer {

  public static DelayTask transfer2TO(DelayTaskDO delayTaskDO) {
    if (delayTaskDO == null) {
      return null;
    }
    DelayTask delayTask = new DelayTask();
    delayTask.setBizType(delayTaskDO.getBizType());
    delayTask.setBizId(delayTaskDO.getBizId());
    delayTask.setStatus(delayTaskDO.getStatus());
    delayTask.setCloseReason(delayTaskDO.getCloseReason());
    delayTask.setDelayStartTime(delayTaskDO.getDelayStartTime());
    delayTask.setDelayEndTime(delayTaskDO.getDelayEndTime());
    delayTask.setDelayTimes(delayTaskDO.getDelayTimes());
    delayTask.setDelayReason(delayTaskDO.getDelayReason());
    delayTask.setCreateTime(delayTaskDO.getCreateTime());
    delayTask.setUpdateTime(delayTaskDO.getUpdateTime());
    return delayTask;
  }

  public static DelayTaskDO transfer2DO(DelayTask delayTask) {
    if (delayTask == null) {
      return null;
    }
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(delayTask.getBizType());
    delayTaskDO.setBizId(delayTask.getBizId());
    delayTaskDO.setStatus(delayTask.getStatus());
    delayTaskDO.setCloseReason(delayTask.getCloseReason());
    delayTaskDO.setDelayStartTime(delayTask.getDelayStartTime());
    delayTaskDO.setDelayEndTime(delayTask.getDelayEndTime());
    delayTaskDO.setDelayTimes(delayTask.getDelayTimes());
    delayTaskDO.setDelayReason(delayTask.getDelayReason());
    delayTaskDO.setCreateTime(delayTask.getCreateTime());
    delayTaskDO.setUpdateTime(delayTask.getUpdateTime());
    return delayTaskDO;
  }

  public static List<DelayTask> transfer2TOList(List<DelayTaskDO> delayTaskDOList) {
    if (CollectionUtils.isEmpty(delayTaskDOList)) {
      return Collections.emptyList();
    }

    List<DelayTask> delayTaskList = new ArrayList<>();
    delayTaskDOList.forEach(delayTaskDO -> delayTaskList.add(transfer2TO(delayTaskDO)));

    return delayTaskList;
  }
}
