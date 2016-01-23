package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.util.TimeUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/22 下午9:51
 */
public class DelayTaskDAOTest extends BaseTest {

  @Resource
  private DelayTaskDAO delayTaskDAO;

  @Test
  public void testInsert() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(10);
    delayTaskDO.setBizId("123");
    delayTaskDO.setBizState(201);
    delayTaskDO.setStatus(10);
    delayTaskDO.setCloseReason(0);
    delayTaskDO.setDelayStartTime(TimeUtils.getDateBySeconds(1445418000));
    delayTaskDO.setDelayEndTime(TimeUtils.getDateBySeconds(1445418000 + 3600));
    delayTaskDO.setDelayTimes(0);
    delayTaskDO.setMsgStatus(0);
    delayTaskDO.setMsgEndTime(TimeUtils.getDateBySeconds(1445418000 + 1000));
    delayTaskDO.setCreateTime(TimeUtils.getDateBySeconds(1445418123));

    int effectNum = delayTaskDAO.insert(delayTaskDO);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testSelectListWithBizTypeAndTimeout() throws Exception {
    int bizType = 10;
    Date timePoint = new Date(116, 7, 1);
    int maxSize = 2;
    List<DelayTaskDO> delayTaskDOs =
        delayTaskDAO.selectListWithBizTypeAndTimeout(bizType, TaskStatus.ACTIVE.code(), timePoint, maxSize);
    Assert.assertEquals(2, delayTaskDOs.size());
  }

  @Test
  public void testSelectListWithBizTypeAndMsgTimeout() throws Exception {
    int bizType = 10;
    Date timePoint = new Date(116, 7, 1);
    int maxSize = 2;

    List<DelayTaskDO> delayTaskDOs =
        delayTaskDAO.selectListWithBizTypeAndMsgTimeout(bizType, TaskStatus.ACTIVE.code(), MsgStatus.ACTIVE.code(), timePoint, maxSize);
    Assert.assertEquals(1, delayTaskDOs.size());
  }

  @Test
  public void testSelectDelayTimesById() {
    int taskId = 322;
    int delayTimes = delayTaskDAO.selectDelayTimesById(taskId);
  }

  @Test
  public void testUpdateOnSuccess() {
    int taskId = 322;

    int effectNum = delayTaskDAO.close(taskId, TaskStatus.ACTIVE.code(),
                       TaskStatus.CLOSED.code(), CloseReason.SUCCESS.code(),
                       TimeUtils.currentDate());
  }

  @Test
  public void testUpdateOnFailure() {
    int taskId = 327;
    int delayTimeIncrement = 5 * 60;

    int effectNum = delayTaskDAO.updateOnRetry(taskId, TaskStatus.ACTIVE.code(), delayTimeIncrement, new Date());
  }

  @Test
  public void testCloseTaskAhead() {
    int bizType = 10;
    String bizId = "123";

    int effectNum = delayTaskDAO.closeTaskAhead(bizType, bizId,
                                                TaskStatus.ACTIVE.code(),
                                                TaskStatus.CLOSED.code(),
                                                CloseReason.AHEAD.code(), TimeUtils.currentDate());
  }

  @Test
  public void testCloseMsg() throws Exception {
    int taskId = 327;
    int msgStatus = 20;

    int effectNum = delayTaskDAO.closeMsg(taskId, TaskStatus.ACTIVE.code(), msgStatus, new Date());
  }

  @Test
  public void testClose() {
    int taskId = 327;

    int effectNum = delayTaskDAO.close(taskId, TaskStatus.ACTIVE.code(),
                                       TaskStatus.CLOSED.code(), CloseReason.SUCCESS.code(),
                                       TimeUtils.currentDate());
  }

  @Test
  public void testUpdateDelayEndTime() throws Exception {
    int bizType = 26;
    String bizId = "E20151130154923037470698";
    int delayTimeIncrement = 5 * 60;
    Date updateTime = new Date();

    int effectNum = delayTaskDAO.updateDelayEndTime(bizType, bizId, TaskStatus.ACTIVE.code(), delayTimeIncrement, updateTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testTryLockByTaskId() throws Exception {
    int taskId = 327;
    Date currentTime = new Date();

    int effectNum = delayTaskDAO.tryLockByTaskId(taskId, TaskStatus.ACTIVE.code(), currentTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testForceLockByTaskId() throws Exception {
    int taskId = 327;
    int internalMinutes = 2;
    Date currentTime = new Date();

    int effectNum = delayTaskDAO.forceLockByTaskId(taskId, TaskStatus.ACTIVE.code(), internalMinutes, currentTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testUnlockByTaskId() throws Exception {
    int taskId = 327;
    Date updateTime = new Date();

    int effectNum = delayTaskDAO.unlockByTaskId(taskId, updateTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testTryLockMsgByTaskId() throws Exception {
    int taskId = 327;
    Date currentTime = new Date();

    int effectNum = delayTaskDAO.tryLockMsgByTaskId(taskId, TaskStatus.ACTIVE.code(), currentTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testForceLockMsgByTaskId() throws Exception {
    int taskId = 327;
    int internalMinutes = 2;
    Date currentTime = new Date();

    int effectNum = delayTaskDAO.forceLockMsgByTaskId(taskId, TaskStatus.ACTIVE.code(), internalMinutes, currentTime);
    Assert.assertEquals(1, effectNum);
  }

  @Test
  public void testUnlockMsgByTaskId() throws Exception {
    int taskId = 327;
    Date updateTime = new Date();

    int effectNum = delayTaskDAO.unlockMsgByTaskId(taskId, updateTime);
    Assert.assertEquals(1, effectNum);
  }
}