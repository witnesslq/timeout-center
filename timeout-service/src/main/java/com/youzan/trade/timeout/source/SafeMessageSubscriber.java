package com.youzan.trade.timeout.source;

import com.youzan.nsq.client.remoting.connector.CustomerConnector;
import com.youzan.nsq.client.remoting.listener.ConnectorListener;
import com.youzan.nsq.client.remoting.listener.NSQEvent;
import com.youzan.trade.util.LogUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author apple created at: 15/10/28 下午6:06
 */
@Slf4j
@Deprecated
public class SafeMessageSubscriber {

  private String NSQ_HOST;
  private int NSQ_PORT;

  private String NSQ_TOPIC_CREATE;
  private String NSQ_CHANNEL_CREATE;

  private String NSQ_TOPIC_UPDATE;
  private String NSQ_CHANNEL_UPDATE;

  private CustomerConnector updateConnector;
  private CustomerConnector createConnector;

  @Resource(name = "safeProcessorImpl")
  private Processor safeProcessor;

//  @PostConstruct
  public void init() {
    initCreateConnector();
    initUpdateConnector();
  }

  public void initCreateConnector() {
    ConnectorListener createListener = event -> {
      if (NSQEvent.READ.equals(event.getType())) {
        String message = event.getMessage();

        LogUtils.info(log, "receive create message: {}", message);

        if ( !safeProcessor.process(message) ) {
          throw new Exception("consuming message failed.");
        }
      }
    };

    createConnector = new CustomerConnector(NSQ_HOST, NSQ_PORT, NSQ_TOPIC_CREATE, NSQ_CHANNEL_CREATE);
    createConnector.setSubListener(createListener);
    createConnector.connect();
  }

  public void initUpdateConnector() {
    ConnectorListener updateListener = event -> {
      if (NSQEvent.READ.equals(event.getType())) {
        String message = event.getMessage();

        LogUtils.info(log, "receive update message: {}", message);

        if ( !safeProcessor.process(message) ) {
          throw new Exception("consuming message failed.");
        }
      }
    };

    updateConnector = new CustomerConnector(NSQ_HOST, NSQ_PORT, NSQ_TOPIC_UPDATE, NSQ_CHANNEL_UPDATE);
    updateConnector.setSubListener(updateListener);
    updateConnector.connect();
  }

  @PreDestroy
  public void destroy() {
    updateConnector.close();
  }

  public void setNsqHost(String nsqHost) {
    NSQ_HOST = nsqHost;
  }

  public void setNsqPort(int nsqPort) {
    NSQ_PORT = nsqPort;
  }

  public void setNsqTopicCreate(String nsqTopicCreate) {
    NSQ_TOPIC_CREATE = nsqTopicCreate;
  }

  public void setNsqTopicUpdate(String nsqTopicUpdate) {
    NSQ_TOPIC_UPDATE = nsqTopicUpdate;
  }

  public void setNsqChannelCreate(String nsqChannelCreate) {
    NSQ_CHANNEL_CREATE = nsqChannelCreate;
  }

  public void setNsqChannelUpdate(String nsqChannelUpdate) {
    NSQ_CHANNEL_UPDATE = nsqChannelUpdate;
  }
}
