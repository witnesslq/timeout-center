package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.constants.CloseReason;
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
  }


  @Test
  public void testSelectListWithTimeout() {
    Date timePoint = new Date();
    List<DelayTaskDO> delayTaskDOList = delayTaskDAO.selectListWithTimeout(timePoint);
  }

  @Test
  public void testSelectListWithBizTypeAndTimeout() throws Exception {
    int bizType = 10;
    Date timePoint = new Date(115, 7, 1);
    int maxSize = 2;
    List<DelayTaskDO> delayTaskDOs =
        delayTaskDAO.selectListWithBizTypeAndTimeout(bizType, timePoint, maxSize);
    Assert.assertEquals(2, delayTaskDOs.size());
  }

  @Test
  public void testSelectListWithMsgTimeout() {
    Date timePoint = new Date();
    List<DelayTaskDO> delayTaskDOList = delayTaskDAO.selectListWithMsgTimeout(timePoint);
  }

  @Test
  public void testSelectDelayTimesById() {
    int taskId = 74;
    int delayTimes = delayTaskDAO.selectDelayTimesById(taskId);
  }

  @Test
  public void testUpdateOnSuccess() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(74);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());
    delayTaskDO.setUpdateTime(new Date());

    int effectNum = delayTaskDAO.close(delayTaskDO);
  }

  @Test
  public void testUpdateOnFailure() {
    int taskId = 75;
    int delayTimeIncrement = 5 * 60;

    int effectNum = delayTaskDAO.updateOnRetry(taskId, delayTimeIncrement, new Date());
  }

  @Test
  public void testCloseTask() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(10);
    delayTaskDO.setBizId("123");
    delayTaskDO.setStatus(20);
    delayTaskDO.setCloseReason(10);
    delayTaskDO.setUpdateTime(new Date());

    int effectNum = delayTaskDAO.closeTaskAhead(delayTaskDO);
  }

  @Test
  public void testCloseMsg() throws Exception {
    int taskId = 75;
    int msgStatus = 20;

    int effectNum = delayTaskDAO.closeMsg(taskId, msgStatus, new Date());
  }

  @Test
  public void testClose() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(57);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());
    delayTaskDO.setUpdateTime(new Date());

    int effectNum = delayTaskDAO.close(delayTaskDO);
  }


}