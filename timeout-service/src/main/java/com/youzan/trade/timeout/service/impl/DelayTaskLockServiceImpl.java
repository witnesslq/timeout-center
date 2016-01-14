package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.timeout.dal.dao.DelayTaskLockDAO;
import com.youzan.trade.timeout.service.DelayTaskLockService;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author apple created at: 15/11/5 下午5:38
 */
@Service
public class DelayTaskLockServiceImpl implements DelayTaskLockService{

  private static final int LOCK_MAX_INTERNAL_IN_MINUTES = 30;

  @Resource
  private DelayTaskLockDAO delayTaskLockDAO;

  @Override
  public boolean lockByLockId(int lockId) {
    if (tryLockByLockId(lockId)) {
      return true;
    }

    if (forceLockByLockId(lockId)) {
      return true;
    }

    return false;
  }

  @Override
  public boolean unlockByLockId(int lockId) {
    return delayTaskLockDAO.unlockByLockId(lockId, TimeUtils.currentDate()) == 1;
  }

  private boolean tryLockByLockId(int lockId) {
    return delayTaskLockDAO.lockByLockId(lockId, TimeUtils.currentDate()) == 1;
  }

  private boolean forceLockByLockId(int lockId) {
    return delayTaskLockDAO.forceLockByLockId(lockId,
                                              LOCK_MAX_INTERNAL_IN_MINUTES,
                                              TimeUtils.currentDate()) == 1;
  }
}
