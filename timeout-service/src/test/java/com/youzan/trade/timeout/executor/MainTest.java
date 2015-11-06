package com.youzan.trade.timeout.executor;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/11/6 下午4:21
 */
public class MainTest {

  @Test
  public void testMain() throws Exception {
    new ClassPathXmlApplicationContext("classpath:test-applicationContext.xml");
  }

  public static void main(String[] args) {
    new ClassPathXmlApplicationContext("classpath:test-applicationContext.xml");
  }
}