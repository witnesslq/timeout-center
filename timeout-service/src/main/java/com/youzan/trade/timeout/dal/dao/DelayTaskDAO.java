package com.youzan.trade.timeout.dal.dao;


import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

  /**
   * 查询在某个时间点后已经超时的任务列表
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithTimeout(Date timePoint);

  /**
   * 查询在某个时间点后消息任务已经超时的任务列表
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithMsgTimeout(Date timePoint);

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

  int closeMsg(@Param("taskId") int taskId, @Param("msgStatus") int msgStatus);

  int updateOnRetry(@Param("taskId") int taskId,
                    @Param("delayTimeIncrement") int delayTimeIncrement);

  int updateMsgOnRetry(@Param("taskId") int taskId,
                       @Param("delayTimeIncrement") int delayTimeIncrement);

  int closeTaskAhead(DelayTaskDO delayTaskDO);
}
