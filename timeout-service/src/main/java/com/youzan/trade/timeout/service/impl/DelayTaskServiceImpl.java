package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.dal.dao.DelayTaskDAO;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.timeout.transfer.DelayTaskDataTransfer;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/23 下午3:19
 */
@Service
public class DelayTaskServiceImpl implements DelayTaskService {

  @Resource
  private DelayTaskDAO delayTaskDAO;

  @Resource
  private DelayTimeStrategy delayTimeStrategy;

  @Override
  public List<DelayTask> getListWithTimeout(int timePoint) {
    return DelayTaskDataTransfer.transfer2TOList(delayTaskDAO.selectListWithTimeout(timePoint));
  }

  @Override
  public List<DelayTask> getListWithTimeoutCurrently() {
    int timePoint = TimeUtils.currentInSeconds();
    return getListWithTimeout(timePoint);
  }

  @Override
  public boolean updateOnSuccess(int taskId) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentInSeconds());

    return delayTaskDAO.updateOnSuccess(delayTaskDO) == 1;
  }

  @Override
  public boolean updateOnFailure(int taskId) {
    int delayTimeIncrement = delayTimeStrategy.getDelayTime(delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateOnFailure(taskId,
                                 delayTimeIncrement,
                                 TimeUtils.currentInSeconds()) == 1;

  }

}
