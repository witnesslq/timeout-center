package com.youzan.trade.timeout.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/11/15 下午8:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-spring-mockito.xml"})
public class DelayTimeStrategyImplTest {

  @Resource
  private DelayTimeStrategyImpl delayTimeStrategy;

  @Test
  public void testGetNextDelayIncrement() throws Exception {
    int delayTimeIncrementInSeconds = delayTimeStrategy.getNextDelayIncrement(1);
    Assert.assertEquals(60, delayTimeIncrementInSeconds);
  }

  @Test
  public void testGetInitialDelayTime() throws Exception {
    int delayTimeInSeconds = delayTimeStrategy.getInitialDelayTime(10, "W123", 201);
    Assert.assertEquals(300, delayTimeInSeconds);
  }
}