package com.youzan.trade.timeout.source;

import com.youzan.nsq.client.remoting.connector.CustomerConnector;
import com.youzan.nsq.client.remoting.listener.ConnectorListener;
import com.youzan.nsq.client.remoting.listener.NSQEvent;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author apple created at: 15/10/28 下午6:06
 */
public class SafeMessageSubscriber {

  private static String NSQ_HOST;
  private static int NSQ_PORT;

  private static String NSQ_TOPIC;
  private static String NSQ_CHANNEL;

  private CustomerConnector connector;

  @Resource
  private Processor safeProcessor;

  @PostConstruct
  public void init() {
    ConnectorListener listener = event -> {
      if (NSQEvent.READ.equals(event.getType())) {
        String message = event.getMessage();

//        System.out.println("SafeMessageSubscriber receive message: " + message);

        if ( !safeProcessor.process(message) ) {
          throw new Exception("consuming message failed.");
        }
      }
    };

    connector = new CustomerConnector(NSQ_HOST, NSQ_PORT, NSQ_TOPIC, NSQ_CHANNEL);
    connector.setSubListener(listener);
    connector.connect();
  }

  @PreDestroy
  public void destroy() {
    connector.close();
  }

  public void setNsqHost(String nsqHost) {
    NSQ_HOST = nsqHost;
  }

  public void setNsqPort(int nsqPort) {
    NSQ_PORT = nsqPort;
  }

  public void setNsqTopic(String nsqTopic) {
    NSQ_TOPIC = nsqTopic;
  }

  public void setNsqChannel(String nsqChannel) {
    NSQ_CHANNEL = nsqChannel;
  }
}
