package com.youzan.trade.timeout;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动spring容器的测试类,不含dubbo服务
 *
 * @author apple created at: 16/1/21 下午9:27
 */
public class MainTest {

  public static void main(String... args) {
    new ClassPathXmlApplicationContext("test-applicationContext.xml");
  }

}
