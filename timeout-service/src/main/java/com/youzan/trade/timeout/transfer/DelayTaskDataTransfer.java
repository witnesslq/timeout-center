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
    delayTask.setId(delayTaskDO.getId());
    delayTask.setBizType(delayTaskDO.getBizType());
    delayTask.setBizId(delayTaskDO.getBizId());
    delayTask.setBizState(delayTaskDO.getBizState());
    delayTask.setBizShardKey(delayTaskDO.getBizShardKey());
    delayTask.setStatus(delayTaskDO.getStatus());
    delayTask.setCloseReason(delayTaskDO.getCloseReason());
    delayTask.setDelayStartTime(delayTaskDO.getDelayStartTime());
    delayTask.setDelayEndTime(delayTaskDO.getDelayEndTime());
    delayTask.setDelayTimes(delayTaskDO.getDelayTimes());
    delayTask.setMsgStatus(delayTaskDO.getMsgStatus());
    delayTask.setMsgEndTime(delayTaskDO.getMsgEndTime());
    delayTask.setCreateTime(delayTaskDO.getCreateTime());
    delayTask.setUpdateTime(delayTaskDO.getUpdateTime());
    delayTask.setSuspendTime(delayTaskDO.getSuspendTime());
    return delayTask;
  }

  public static DelayTaskDO transfer2DO(DelayTask delayTask) {
    if (delayTask == null) {
      return null;
    }
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(delayTask.getId());
    delayTaskDO.setBizType(delayTask.getBizType());
    delayTaskDO.setBizId(delayTask.getBizId());
    delayTaskDO.setBizState(delayTask.getBizState());
    delayTaskDO.setBizShardKey(delayTask.getBizShardKey());
    delayTaskDO.setStatus(delayTask.getStatus());
    delayTaskDO.setCloseReason(delayTask.getCloseReason());
    delayTaskDO.setDelayStartTime(delayTask.getDelayStartTime());
    delayTaskDO.setDelayEndTime(delayTask.getDelayEndTime());
    delayTaskDO.setDelayTimes(delayTask.getDelayTimes());
    delayTaskDO.setMsgStatus(delayTask.getMsgStatus());
    delayTaskDO.setMsgEndTime(delayTask.getMsgEndTime());
    delayTaskDO.setCreateTime(delayTask.getCreateTime());
    delayTaskDO.setUpdateTime(delayTask.getUpdateTime());
    delayTaskDO.setSuspendTime(delayTask.getSuspendTime());
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
