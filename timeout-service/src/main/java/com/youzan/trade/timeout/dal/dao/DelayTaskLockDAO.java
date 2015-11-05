package com.youzan.trade.timeout.dal.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author apple created at: 15/11/2 下午5:18
 */
public interface DelayTaskLockDAO {

  /**
   * 获取指定的锁
   *
   * @param lockId delay_task_lock表的lock_id
   * @param updateTime 更新时间
   * @return
   */
  int lockByLockId(@Param("lockId") int lockId, @Param("updateTime") Date updateTime);

  /**
   * 强制获取指定的锁
   * 如果指定的锁处于锁定状态且超过internalMinutes没有更新,则强制获取
   *
   * @param lockId delay_task_lock表的lock_id
   * @param internalMinutes 最长间隔时间,单位是分钟
   * @param currentTime 更新时间
   * @return
   */
  int forceLockByLockId(@Param("lockId") int lockId,
                        @Param("internalMinutes") int internalMinutes,
                        @Param("currentTime") Date currentTime);

  /**
   * 释放指定的锁
   *
   * @param lockId delay_task_lock表的lock_id
   * @param updateTime 更新时间
   * @return
   */
  int unlockByLockId(@Param("lockId") int lockId, @Param("updateTime") Date updateTime);

}
