package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author apple created at: 15/10/22 下午6:05
 */
public interface DelayTaskDAO {

  List<DelayTaskDO> selectListWithTimeout(int timePoint);

  /**
   * 根据taskId查询任务已经超时的次数
   *
   * @param taskId
   * @return
   */
  int selectDelayTimesById(int taskId);

  int updateOnSuccess(DelayTaskDO delayTaskDO);

  int updateOnFailure(@Param("taskId") int taskId,
                      @Param("delayTimeIncrement") int delayTimeIncrement,
                      @Param("updateTime") int updateTime);

}
