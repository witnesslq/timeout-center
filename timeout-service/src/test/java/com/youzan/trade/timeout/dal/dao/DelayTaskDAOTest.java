package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author apple created at: 15/10/22 下午9:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class DelayTaskDAOTest {

  @Resource
  private DelayTaskDAO delayTaskDAO;


  @Test
  public void testSelectAll() throws Exception {
    //
    int timePoint = 1445418000;
    List<DelayTaskDO> delayTaskDOList = delayTaskDAO.selectListWithTimeout(timePoint);

    System.out.println();
  }
}