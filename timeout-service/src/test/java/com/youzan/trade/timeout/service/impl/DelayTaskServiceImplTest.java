package com.youzan.trade.timeout.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/10/23 下午7:42
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class DelayTaskServiceImplTest {

  @Resource
  private DelayTaskServiceImpl delayTaskService;

  @Test
  public void testGetListWithTimeout() throws Exception {

  }

  @Test
  public void testGetListWithTimeoutCurrently() throws Exception {
    delayTaskService.getListWithTimeoutCurrently();
  }
}