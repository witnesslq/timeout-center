package com.youzan.trade.timeout.dal.dao;

import org.junit.Test;

import java.util.Date;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/11/5 下午9:11
 */
public class DelayTaskLockDAOTest {

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

  }

  @Test
  public void testUnlockByLockId() throws Exception {

  }
}