package com.youzan.trade.timeout.service.impl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/11/15 下午9:34
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-applicationContext.xml", "classpath:test-spring-mockito.xml"})
public class MsgDelayTimeStrategyImplTest {

  @Resource
  private MsgDelayTimeStrategyImpl msgDelayTimeStrategy;

  @Test
  public void testGetNextDelayIncrement() throws Exception {
    int nextDelayIncrement = msgDelayTimeStrategy.getNextDelayIncrement(1);
    Assert.assertEquals(60, nextDelayIncrement);
  }

  @Test
  public void testGetInitialDelayTime() throws Exception {
    int initialDelayTime = msgDelayTimeStrategy.getInitialDelayTime(10, "W123", 201);
    Assert.assertEquals(60, initialDelayTime);
  }
}