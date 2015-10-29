package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.util.TimeUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/10/22 下午9:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class DelayTaskDAOTest {

  @Resource
  private DelayTaskDAO delayTaskDAO;

  @Test
  public void testInsert() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(1);
    delayTaskDO.setBizId("123");
    delayTaskDO.setBizState(201);
    delayTaskDO.setStatus(10);
    delayTaskDO.setCloseReason(0);
    delayTaskDO.setDelayStartTime(1445418000);
    delayTaskDO.setDelayEndTime(1445418000 + 3600);
    delayTaskDO.setDelayTimes(0);
    delayTaskDO.setCreateTime(1445418123);
    delayTaskDO.setUpdateTime(1445418456);

    int effectNum = delayTaskDAO.insert(delayTaskDO);
  }


  @Test
  public void testSelectAll() throws Exception {
    //
    int timePoint = 1445418000;
    List<DelayTaskDO> delayTaskDOList = delayTaskDAO.selectListWithTimeout(timePoint);

    System.out.println();
  }

  @Test
  public void testSelectDelayTimesById() {
    int taskId = 1;
    int delayTimes = delayTaskDAO.selectDelayTimesById(taskId);
  }

  @Test
  public void testUpdateOnSuccess() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(1);
    delayTaskDO.setStatus(TaskStatus.CLOSED.code());
    delayTaskDO.setCloseReason(CloseReason.SUCCESS.code());
    delayTaskDO.setUpdateTime(TimeUtils.currentInSeconds());

    int effectNum = delayTaskDAO.updateOnSuccess(delayTaskDO);
  }

  @Test
  public void testUpdateOnFailure() {
    int taskId = 2;
    int delayTimeIncrement = 5 * 60;
    int updateTime = 1445418001;

    int effectNum = delayTaskDAO.updateOnFailure(taskId, delayTimeIncrement, updateTime);
  }

  @Test
  public void testCloseTask() {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setBizType(1);
    delayTaskDO.setBizId("123");
    delayTaskDO.setStatus(20);
    delayTaskDO.setCloseReason(10);
    delayTaskDO.setUpdateTime(1445418456);

    int effectNum = delayTaskDAO.closeTask(delayTaskDO);
  }

}