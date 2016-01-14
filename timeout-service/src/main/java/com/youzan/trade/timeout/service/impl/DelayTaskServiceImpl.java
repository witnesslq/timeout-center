package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.constants.TimeConstants;
import com.youzan.trade.timeout.dal.dao.DelayTaskDAO;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.timeout.transfer.DelayTaskDataTransfer;
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
  public boolean save(DelayTask delayTask) {
    LogUtils.info(log, "保存一个延时任务: {}", delayTask);

    return delayTaskDAO.insert(DelayTaskDataTransfer.transfer2DO(delayTask)) == 1;
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
  public List<DelayTask> getListWithBizTypeAndTimeout(int bizType, Date timePoint, int maxSize) {
    return DelayTaskDataTransfer
        .transfer2TOList(delayTaskDAO.selectListWithBizTypeAndTimeout(bizType, timePoint, maxSize));
  }

  @Override
  public List<DelayTask> getListWithBizTypeAndTimeoutCurrently(int bizType, int maxSize) {
    return getListWithBizTypeAndTimeout(bizType, Calendar.getInstance().getTime(), maxSize);
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
  public List<DelayTask> getListWithBizTypeAndMsgTimeout(int bizType, Date timePoint, int maxSize) {
    return DelayTaskDataTransfer.
        transfer2TOList(delayTaskDAO.selectListWithBizTypeAndMsgTimeout(bizType, timePoint, maxSize));
  }

  @Override
  public List<DelayTask> getListWithBizTypeAndMsgTimeoutCurrently(int bizType, int maxSize) {
    return getListWithBizTypeAndMsgTimeout(bizType, Calendar.getInstance().getTime(), maxSize);
  }

  @Override
  public boolean closeOnSuccess(int taskId,CloseReason closeReason) {
    LogUtils.info(log, "超时任务执行成功, 关闭超时任务, taskId: {}", taskId);

    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(closeReason.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentDate());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean closeOnNoRetry(int taskId) {
    LogUtils.info(log, "超时任务执行失败, 关闭超时任务, 不再重试, taskId: {}", taskId);

    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(taskId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.FAILURE_NO_RETRY.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentDate());

    return delayTaskDAO.close(delayTaskDO) == 1;
  }

  @Override
  public boolean updateOnRetry(int taskId) {
    LogUtils.info(log, "超时任务执行失败, 更新超时任务, taskId: {}", taskId);

    int delayTimeIncrement = delayTimeStrategy.getNextDelayIncrement(
        delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateOnRetry(taskId, delayTimeIncrement, TimeUtils.currentDate()) == 1;

  }

  @Override
  public boolean closeMsgOnSuccess(int taskId) {
    LogUtils.info(log, "消息任务执行成功并关闭, taskId: {}", taskId);

    return delayTaskDAO.closeMsg(taskId, MsgStatus.CLOSED.code(), TimeUtils.currentDate()) == 1;
  }

  @Override
  public boolean closeMsgOnNoRetry(int taskId) {
    LogUtils.info(log, "执行消息任务失败, 关闭消息任务, 不再重试, taskId: {}", taskId);

    return delayTaskDAO.closeMsg(taskId, MsgStatus.CLOSED.code(), TimeUtils.currentDate()) == 1;
  }

  @Override
  public boolean updateMsgOnRetry(int taskId) {
    LogUtils.info(log, "执行消息任务失败, 更新消息任务, taskId: {}", taskId);

    int delayTimeIncrement =
        msgDelayTimeStrategy.getNextDelayIncrement(delayTaskDAO.selectDelayTimesById(taskId));
    return delayTaskDAO.updateMsgOnRetry(taskId, delayTimeIncrement, TimeUtils.currentDate()) == 1;
  }

  @Override
  public boolean closeTaskByBizTypeAndBizId(int bizType, String bizId) {
    LogUtils.info(log, "根据业务类型和业务id关闭延时任务, bizType: {}, bizId: {}", bizType, bizId);

    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(bizType);
    delayTaskDO.setBizId(bizId);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.AHEAD.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentDate());

    // 因为不确定对应的延时任务数量
    return delayTaskDAO.closeTaskAhead(delayTaskDO) >= 0;
  }

  @Override
  public DelayTask getTaskByBizTypeAndBizId(int bizType, String bizId) {
    LogUtils.info(log, "根据业务类型和业务ID获取任务, bizType: {}, bizId: {}", bizType, bizId);

    List<DelayTaskDO> delayTaskDOs = delayTaskDAO.selectListByBizTypeAndBizId(bizType, bizId);

    return CollectionUtils.isNotEmpty(delayTaskDOs)
           ? DelayTaskDataTransfer.transfer2TO(delayTaskDOs.get(0)) : null;
  }

  @Override
  public boolean suspendTask(DelayTask task) {
    LogUtils.info(log, "[Suspend Task], taskId={}, fromStatus={}", task.getId(), task.getStatus());

    if (isSuspendable(task)) {
      LogUtils.info(log, "[Suspend Task], taskId={}, toStatus={}", task.getId(),
                    TaskStatus.SUSPENDED.code());

      Date currentTime = TimeUtils.currentDate();

      return 0 < delayTaskDAO
          .updateSuspendTime(task.getId(),
                             TaskStatus.SUSPENDED.code(),
                             currentTime,
                             currentTime
          );
    }
    return false;
  }

  /**
   * 判断任务是否可以被中断。
   * 只有处于激活状态的任务可以被中断
   * @param task
   * @return
   */
  private boolean isSuspendable(DelayTask task) {
    return task != null && task.getStatus() != null && TaskStatus.ACTIVE.code() == task.getStatus();
  }

  @Override
  public boolean resumeTask(DelayTask task, long suspendPeriod) {
    LogUtils.info(log, "[Resume Task]taskId={},fromStatus={},fromEndTime={},fromMsgEndTime={}",
                  task.getId(), task.getStatus(), task.getDelayEndTime(), task.getMsgEndTime());
    if (isResumable(task)) {
      if (refreshEndTime(task, suspendPeriod)) {
        LogUtils.info(log, "[Resume Task]taskId={},toStatus={},toEndTime={},toMsgEndTime={}",
                      task.getId(), TaskStatus.ACTIVE.code(), task.getDelayEndTime(),
                      task.getMsgEndTime());
        return 0 < delayTaskDAO.updateStatusAndEndTime(task.getId(), TaskStatus.ACTIVE.code(),
                                                       task.getDelayEndTime(),
                                                       task.getMsgEndTime(),
                                                       TimeUtils.currentDate());
      }
    }
    return false;
  }


  @Override
  public Integer increaseDelayEndTimeByBizTypeAndBizId(int bizType, String bizId,
                                                       int incrementInDays) {
    LogUtils.info(log, "根据业务类型和业务id延长任务到期时间, bizType: {}, bizId: {}", bizType, bizId);

    DelayTask delayTask = getTaskByBizTypeAndBizId(bizType, bizId);
    if (!canDelayTaskToBeIncreased(delayTask, bizId, bizType)) {

      return null;
    }
    Integer increasedDelayEndTime = calIncreasedDelayEndTime(incrementInDays, delayTask);
    // 因为bizType + bizId不构成唯一索引
    if (delayTaskDAO.updateDelayEndTime(bizType, bizId,
                                        TimeConstants.ONE_DAY_IN_SECONDS * incrementInDays,
                                        TimeUtils.currentDate()) > 0) {
      return increasedDelayEndTime;
    } else {
      LogUtils.error(log, "[FAIL]Increase delaytask end time failed.bizId={},bizType={}", bizId,
                     bizType);
      return null;
    }
  }

  private boolean canDelayTaskToBeIncreased(DelayTask delayTask,String bizId,Integer bizType) {
    if (delayTask == null) {
      LogUtils.warn(log, "DelayTask not found.bizId={},bizType={}",bizId,bizType);
      return false;
    }
    if (delayTask.getDelayEndTime() == null) {
      LogUtils.warn(log, "[IncreaseEndTime]Invalid delayEndTime.bizId={},bizType={}",
                    delayTask.getBizId(), delayTask.getBizType());
      return false;
    }
    if (delayTask.getStatus() == null || TaskStatus.ACTIVE.code() != delayTask.getStatus()) {
      LogUtils.warn(log, "[IncreaseEndTime]Invalid task status={},bizId={},bizType={}",
                    delayTask.getStatus(), delayTask.getBizId(), delayTask.getBizType());
      return false;
    }
    return true;
  }

  private int calIncreasedDelayEndTime(int incrementInDays, DelayTask delayTask) {
    return (int) delayTask.getDelayEndTime().getTime() / 1000
     + TimeConstants.ONE_DAY_IN_SECONDS * incrementInDays;
  }

  private boolean refreshEndTime(DelayTask task, long suspendedTime) {
    Date endTime = task.getDelayEndTime();
    Date msgEndTime = task.getMsgEndTime();
    if (endTime == null) {
      LogUtils.error(log,
                     "Invalid suspended task.endTime shouldn't be blank.taskId={}", task.getId());
      return false;
    }
    Date refreshedEndTime = TimeUtils.plusMilliSecond(endTime, suspendedTime);
    task.setDelayEndTime(refreshedEndTime);
    if (msgEndTime != null && Objects.equals(MsgStatus.ACTIVE.code(), task.getMsgStatus())) {
      Date refreshMsgEndTime = TimeUtils.plusMilliSecond(msgEndTime, suspendedTime);
      task.setMsgEndTime(refreshMsgEndTime);
    }
    return true;
  }

  /**
   * 判断任务是否可以被恢复
   */
  private boolean isResumable(DelayTask task) {
    return task.getStatus() == TaskStatus.SUSPENDED.code();
  }


}
