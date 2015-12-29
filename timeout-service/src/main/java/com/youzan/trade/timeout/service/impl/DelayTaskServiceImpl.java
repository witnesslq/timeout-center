package com.youzan.trade.timeout.service.impl;

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
  public boolean suspendTask(DelayTask task) {
    return false;
  }

  @Override
  public boolean resumeTask(DelayTask task) {
    return false;
  }

  @Override
  public boolean enlargeTask(DelayTask task) {
    return false;
  }

}
