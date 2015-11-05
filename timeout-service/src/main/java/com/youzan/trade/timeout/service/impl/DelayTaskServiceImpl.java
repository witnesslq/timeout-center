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
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author apple created at: 15/10/23 下午3:19
 */
@Slf4j
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
    delayTaskDO.setDelayStartTime(TimeUtils.getDateBySeconds(safe.getRecordTime()));
    delayTaskDO.setDelayEndTime(TimeUtils.getDateBySeconds(safe.getRecordTime() + delayTimeStrategy
        .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState())));
    delayTaskDO.setDelayTimes(Constants.INITIAL_DELAY_TIMES);

    if (safe.isNeedMsg()) {
      delayTaskDO.setMsgStatus(MsgStatus.ACTIVE.code());
      delayTaskDO.setMsgEndTime(TimeUtils.getDateBySeconds(safe.getRecordTime() + msgDelayTimeStrategy
          .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState())));
    } else {
      delayTaskDO.setMsgStatus(MsgStatus.NONE.code());
      delayTaskDO.setMsgEndTime(TimeUtils.getDateBySeconds(Constants.INITIAL_MSG_END_TIME));
    }

    delayTaskDO.setCreateTime(TimeUtils.getDateBySeconds(TimeUtils.currentInSeconds()));

    return delayTaskDAO.insert(delayTaskDO) == 1;
  }

  @Override
  public List<DelayTask> getListWithTimeout(Date timePoint) {
    return DelayTaskDataTransfer.transfer2TOList(delayTaskDAO.selectListWithTimeout(timePoint));
  }

  @Override
  public List<DelayTask> getListWithTimeoutCurrently() {
    return getListWithTimeout(Calendar.getInstance().getTime());
  }

  @Override
  public List<DelayTask> getListWithMsgTimeout(Date timePoint) {
    return DelayTaskDataTransfer.transfer2TOList(delayTaskDAO.selectListWithMsgTimeout(timePoint));
  }

  @Override
  public List<DelayTask> getListWithMsgTimeoutCurrently() {
    return getListWithMsgTimeout(Calendar.getInstance().getTime());
  }

  @Override
  public boolean closeOnSuccess(int taskId) {
    LogUtils.info(log, "超时任务执行成功, 关闭超时任务, taskId: {}", taskId);

    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean closeOnNoRetry(int taskId) {
    LogUtils.info(log, "超时任务执行失败, 关闭超时任务, 不再重试, taskId: {}", taskId);

    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.FAILURE_NO_RETRY.code());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean updateOnRetry(int taskId) {
    LogUtils.info(log, "超时任务执行失败, 更新超时任务, taskId: {}", taskId);

    int delayTimeIncrement = delayTimeStrategy.getNextDelayIncrement(
        delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateOnRetry(taskId, delayTimeIncrement) == 1;

  }

  @Override
  public boolean closeMsgOnSuccess(int taskId) {
    LogUtils.info(log, "消息任务执行成功并关闭, taskId: {}", taskId);

    return delayTaskDAO.closeMsg(taskId, MsgStatus.CLOSED.code()) == 1;
  }

  @Override
  public boolean closeMsgOnNoRetry(int taskId) {
    LogUtils.info(log, "执行消息任务失败, 关闭消息任务, 不再重试, taskId: {}", taskId);

    return delayTaskDAO.closeMsg(taskId, MsgStatus.CLOSED.code()) == 1;
  }

  @Override
  public boolean updateMsgOnRetry(int taskId) {
    LogUtils.info(log, "执行消息任务失败, 更新消息任务, taskId: {}", taskId);

    int delayTimeIncrement =
        msgDelayTimeStrategy.getNextDelayIncrement(delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateMsgOnRetry(taskId, delayTimeIncrement) == 1;
  }

  @Override
  public boolean closeTaskByBizTypeAndBizId(int bizType, String bizId) {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(bizType);
    delayTaskDO.setBizId(bizId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.AHEAD.code());

    // 因为不确定对应的延时任务数量
    return delayTaskDAO.closeTaskAhead(delayTaskDO) >= 0;
  }

}
