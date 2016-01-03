package com.youzan.trade.common.nsq;

import com.youzan.nsq.client.remoting.connector.CustomerConnector;
import com.youzan.nsq.client.remoting.listener.ConnectorListener;
import com.youzan.nsq.client.remoting.listener.NSQEvent;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;

import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Slf4j
public class NSQSubConnectorBuilder {

  private String nsqHost;
  private int nsqPort;

  private String nsqTopic;
  private String nsqChannel;

  private Processor processor;

  private CustomerConnector connector;

  public void buildAndRun() {
    try {
      ConnectorListener subListener = createConnectorListener();

      connector = new CustomerConnector(nsqHost, nsqPort, nsqTopic, nsqChannel);
      connector.setSubListener(subListener);
      connector.connect();
    } catch (Exception e) {
      LogUtils.error(log,
                     "Start connector failed.host=" + nsqHost + " port=" + nsqPort + " topic="
                     + nsqTopic + " channel=" + nsqChannel, e);
    }
  }

  private ConnectorListener createConnectorListener() throws Exception {
    return event -> {
      if (NSQEvent.READ.equals(event.getType())) {
        String message = event.getMessage();

        LogUtils.info(log, "[{}-{}]receive create message: {}", nsqTopic, nsqChannel, message);

        if (!processor.process(message)) {
          //处理失败抛出异常，NSQClient捕获后会将消息重新添加到NSQ中便于下次重新消费处理
          throw new Exception("consuming message failed.");
        }
      }
    };
  }

  @PreDestroy
  public void destroy() {
    if (connector != null) {
      try {
        connector.close();
      } catch (Exception e) {
        LogUtils.error(log, "Destroy connector failed.");
      }
    }
  }

  public void setNsqHost(String nsqHost) {
    this.nsqHost = nsqHost;
  }

  public void setNsqPort(int nsqPort) {
    this.nsqPort = nsqPort;
  }

  public void setNsqTopic(String nsqTopicUpdate) {
    nsqTopic = nsqTopicUpdate;
  }

  public void setNsqChannel(String nsqChannelCreate) {
    nsqChannel = nsqChannelCreate;
  }

  public void setProcessor(Processor processor) {
    this.processor = processor;
  }
}
