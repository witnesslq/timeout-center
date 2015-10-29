package com.youzan.trade.timeout.source;

/**
 * @author apple created at: 15/10/28 上午9:59
 */
public interface Processor {

  /**
   * 处理从nsq获取的消息
   *
   * @param message nsq消息
   * @return
   */
  boolean process(String message);
}
