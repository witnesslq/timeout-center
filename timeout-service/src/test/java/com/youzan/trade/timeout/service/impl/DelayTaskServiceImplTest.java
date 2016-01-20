package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.dal.dao.DelayTaskDAO;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;

import com.google.common.collect.Lists;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * @author apple created at: 15/10/23 下午7:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-spring-mockito.xml"})
public class DelayTaskServiceImplTest {

  @Resource
  private DelayTaskServiceImpl delayTaskService;

  @Resource
  private DelayTaskDAO delayTaskDAO;

  private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

  private DelayTaskDO generateDelayTaskDO() throws ParseException {
    DelayTaskDO delayTaskDO = new DelayTaskDO();
    delayTaskDO.setId(1);
    delayTaskDO.setBizType(10);
    delayTaskDO.setBizId("W123");
    delayTaskDO.setBizState(10);
    delayTaskDO.setStatus(10);
    delayTaskDO.setCloseReason(0);
    delayTaskDO.setDelayStartTime(format.parse("2015-10-01"));
    delayTaskDO.setDelayEndTime(format.parse("2015-11-01"));
    delayTaskDO.setDelayTimes(0);
    delayTaskDO.setMsgStatus(0);
    delayTaskDO.setMsgEndTime(format.parse("2015-10-03"));
    delayTaskDO.setCreateTime(format.parse("2015-10-01"));
    delayTaskDO.setUpdateTime(format.parse("2015-10-01"));

    return delayTaskDO;
  }

  private DelayTask generateDelayTask() throws ParseException {
    DelayTask delayTask = new DelayTask();
    delayTask.setId(1);
    delayTask.setBizType(10);
    delayTask.setBizId("W123");
    delayTask.setBizState(10);
    delayTask.setStatus(10);
    delayTask.setCloseReason(0);
    delayTask.setDelayStartTime(format.parse("2015-10-01"));
    delayTask.setDelayEndTime(format.parse("2015-11-01"));
    delayTask.setDelayTimes(0);
    delayTask.setMsgStatus(0);
    delayTask.setMsgEndTime(format.parse("2015-10-03"));
    delayTask.setCreateTime(format.parse("2015-10-01"));
    delayTask.setUpdateTime(format.parse("2015-10-01"));

    return delayTask;
  }

}