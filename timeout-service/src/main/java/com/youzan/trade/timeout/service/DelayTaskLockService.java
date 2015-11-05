package com.youzan.trade.timeout.service;

/**
 * @author apple created at: 15/11/5 下午5:35
 */
public interface DelayTaskLockService {

  /**
   * 获取指定的锁
   *
   * @param lockId 锁的id
   * @return
   */
  boolean lockByLockId(int lockId);

  /**
   * 释放指定的锁
   *
   * @param lockId
   * @return
   */
  boolean unlockByLockId(int lockId);

}
