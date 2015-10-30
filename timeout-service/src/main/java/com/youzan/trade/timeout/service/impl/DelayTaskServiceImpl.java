package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.dal.dao.DelayTaskDAO;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
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

  @Resource(name = "delayTimeStrategyImpl")
  private DelayTimeStrategy delayTimeStrategy;

  @Resource(name = "msgDelayTimeStrategyImpl")
  private DelayTimeStrategy msgDelayTimeStrategy;

  @Override
  public boolean saveBySafe(Safe safe) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(BizType.SAFE.code());
    delayTaskDO.setBizId(safe.getSafeNo());
    delayTaskDO.setBizState(safe.getState());
    delayTaskDO.setStatus(TaskStatus.ACTIVE.code());
    delayTaskDO.setCloseReason(CloseReason.NOT_CLOSED.code());
    delayTaskDO.setDelayStartTime(safe.getRecordTime());
    delayTaskDO.setDelayEndTime(safe.getRecordTime() + delayTimeStrategy
        .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState()));
    delayTaskDO.setDelayTimes(Constants.INITIAL_DELAY_TIMES);

    if (safe.isNeedMsg()) {
      delayTaskDO.setMsgStatus(MsgStatus.ACTIVE.code());
      delayTaskDO.setMsgEndTime(safe.getRecordTime() + msgDelayTimeStrategy
          .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState()));
    } else {
      delayTaskDO.setMsgStatus(MsgStatus.NONE.code());
      delayTaskDO.setMsgEndTime(Constants.INITIAL_MSG_END_TIME);
    }

    delayTaskDO.setCreateTime(TimeUtils.currentInSeconds());
    delayTaskDO.setUpdateTime(Constants.INITIAL_UPDATE_TIME);

    return delayTaskDAO.insert(delayTaskDO) == 1;
  }

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
  public boolean closeOnSuccess(int taskId) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentInSeconds());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean closeOnNoRetry(int taskId) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.FAILURE_NO_RETRY.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentInSeconds());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean updateOnRetry(int taskId) {
    int delayTimeIncrement = delayTimeStrategy.getNextDelayIncrement(
        delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateOnRetry(taskId,
                                      delayTimeIncrement,
                                      TimeUtils.currentInSeconds()) == 1;

  }

  @Override
  public boolean closeTaskAhead(int bizType, String bizId) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(bizType);
    delayTaskDO.setBizId(bizId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.AHEAD.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentInSeconds());

    // 因为不确定对应的延时任务数量
    return delayTaskDAO.closeTask(delayTaskDO) >= 0;
  }

}
