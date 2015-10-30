package com.youzan.trade.timeout.source;

import com.youzan.nsq.client.remoting.connector.CustomerConnector;
import com.youzan.nsq.client.remoting.listener.ConnectorListener;
import com.youzan.nsq.client.remoting.listener.NSQEvent;
import com.youzan.trade.timeout.model.Safe;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author apple created at: 15/10/27 下午3:53
 */
public class ExampleSubscriber {

  public static final String SAFE_CHANGE_TOPIC = "binglong_safe_change";

  public static void main(String[] args) throws Throwable {

//        start("QITest");
//        start("QITest1111111");
//        start("QJTestTopic");
//        start("testTopic");
//    start("binlog_order_paysuccess");
    start(SAFE_CHANGE_TOPIC);

  }

  public static void start(String topic) throws Throwable {
    ConnectorListener listener = new ConnectorListener() {
      @Override
      public void handleEvent(NSQEvent event) throws Exception {
        if (NSQEvent.READ.equals(event.getType())) {
          String message = event.getMessage();

          // processor

        }
      }
    };

    CustomerConnector connector = new CustomerConnector("192.168.66.202", 4161, topic, "timeout_safe_change");
    connector.setSubListener(listener);
    connector.connect();

    //NSQConnector connector = new CustomerConnector("127.0.0.1",4150,topic,"default",100);
    //connector.setSubListener(listener);
    //connector.connect();

    //Thread.sleep(60*1000);
    //connector.close();
  }
}
