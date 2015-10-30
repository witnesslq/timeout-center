package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author apple created at: 15/10/22 下午6:05
 */
public interface DelayTaskDAO {

  /**
   * 保存一条记录
   *
   * @param delayTaskDO
   * @return
   */
  int insert(DelayTaskDO delayTaskDO);

  List<DelayTaskDO> selectListWithTimeout(int timePoint);

  /**
   * 根据taskId查询任务已经超时的次数
   *
   * @param taskId
   * @return
   */
  int selectDelayTimesById(int taskId);

  /**
   * 当
   *
   * @param delayTaskDO
   * @return
   */
  int close(DelayTaskDO delayTaskDO);

  int updateOnRetry(@Param("taskId") int taskId,
                    @Param("delayTimeIncrement") int delayTimeIncrement,
                    @Param("updateTime") int updateTime);

  int closeTask(DelayTaskDO delayTaskDO);
}
