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
   * 根据业务id及业务类型获取唯一任务记录
   * @param bizId
   * @param bizType
   * @return
   */
  List<DelayTaskDO> getTaskByBizIdAndBizType(@Param("bizId") String bizId, @Param("bizType") int bizType);


  /**
   * 查询在某个时间点后已经超时的任务列表
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithTimeout(Date timePoint);

  /**
   * 查询某个业务在某个时间点后已经超时的任务列表
   * 按业务域区分
   *
   * @param bizType 业务类型
   * @param timePoint 某个时间点
   * @param maxSize 返回记录数的最大值
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithBizTypeAndTimeout(@Param("bizType") int bizType,
                                                    @Param("timePoint") Date timePoint,
                                                    @Param("maxSize") int maxSize);

  /**
   * 查询在某个时间点后消息任务已经超时的任务列表
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithMsgTimeout(Date timePoint);

  /**
   * 查询某个业务在某个时间点后消息任务已经超时且没有完成的任务列表
   *
   * @param bizType 业务类型
   * @param timePoint 某个时间点
   * @param maxSize 返回记录数的最大值
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithBizTypeAndMsgTimeout(@Param("bizType") int bizType,
                                                    @Param("timePoint") Date timePoint,
                                                    @Param("maxSize") int maxSize);

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

  int closeMsg(@Param("taskId") int taskId,
               @Param("msgStatus") int msgStatus,
               @Param("updateTime") Date updateTime);

  int updateOnRetry(@Param("taskId") int taskId,
                    @Param("delayTimeIncrement") int delayTimeIncrement,
                    @Param("updateTime") Date updateTime);

  int updateMsgOnRetry(@Param("taskId") int taskId,
                       @Param("delayTimeIncrement") int delayTimeIncrement,
                       @Param("updateTime") Date updateTime);

  int updateSuspendTime(@Param("taskId")int taskId,
                        @Param("status")int status,
                        @Param("suspendTime")Date suspendTime,
                        @Param("updateTime")Date updateTime);

  /**
   *
   * @param taskId
   * @param status
   * @param delayTaskEndTime
   * @param delayMsgEndTime 如果没有消息任务，该字段可能为空
   * @return
   */
  int updateStatusAndEndTime(@Param("taskId") int taskId,
                             @Param("status") int status,
                             @Param("delayEndTime") Date delayTaskEndTime,
                             @Param("msgEndTime")Date delayMsgEndTime,
                             @Param("updateTime")Date updateTime);

  int closeTaskAhead(DelayTaskDO delayTaskDO);
}
