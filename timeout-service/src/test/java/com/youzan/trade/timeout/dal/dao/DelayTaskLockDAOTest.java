package com.youzan.trade.timeout.dal.dao;

import org.junit.Test;

import java.util.Date;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/11/5 下午9:11
 */
public class DelayTaskLockDAOTest extends BaseTest {

  @Resource
  private DelayTaskLockDAO delayTaskLockDAO;

  @Test
  public void testLockByLockId() throws Exception {
    int lockId = 1;
    Date updateTime = new Date();

    delayTaskLockDAO.lockByLockId(lockId, updateTime);
  }

  @Test
  public void testForceLockByLockId() throws Exception {
    int lockId = 1;
    int internalMinutes = 10;
    Date currentTime = new Date();

    delayTaskLockDAO.forceLockByLockId(lockId, internalMinutes, currentTime);
  }

  @Test
  public void testUnlockByLockId() throws Exception {
    int lockId = 1;
    Date updateTime = new Date();

    delayTaskLockDAO.unlockByLockId(lockId, updateTime);
  }
}