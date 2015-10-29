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
@Component
public class SafeMessageSubscriber {

  public static final String NSQ_HOST = "192.168.66.202";
  public static final int NSQ_PORT = 4161;

  public static final String SAFE_CHANGE_TOPIC = "binglong_safe_change";
  public static final String SAFE_CHANGE_CHANNEL = "timeout_safe_change";

  public CustomerConnector connector;

  @Resource
  private Processor safeProcessor;

  @PostConstruct
  public void init() {
    ConnectorListener listener = event -> {
      if (NSQEvent.READ.equals(event.getType())) {
        String message = event.getMessage();

        System.out.println("SafeMessageSubscriber receive message: " + message);

        if ( !safeProcessor.process(message) ) {
          throw new Exception("consuming message failed.");
        }
      }
    };

    connector = new CustomerConnector(NSQ_HOST, NSQ_PORT, SAFE_CHANGE_TOPIC, SAFE_CHANGE_CHANNEL);
    connector.setSubListener(listener);
    connector.connect();
  }

  @PreDestroy
  public void destroy() {
    connector.close();
  }
}
