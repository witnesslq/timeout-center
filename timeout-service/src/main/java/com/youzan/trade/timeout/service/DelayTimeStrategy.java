package com.youzan.trade.timeout.service;

/**
 * @author apple created at: 15/10/26 下午7:42
 */
public interface DelayTimeStrategy {

  /**
   * 获取下次超时到期时间的增量
   * 以秒为单位
   *
   * @param delayTimes 任务已经超时的次数
   * @return
   */
  int getDelayTime(int delayTimes);
}
