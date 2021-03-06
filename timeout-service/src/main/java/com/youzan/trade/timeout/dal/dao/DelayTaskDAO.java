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
   */
  int insert(DelayTaskDO delayTaskDO);

  /**
   * 根据业务id及业务类型获取任务记录列表
   *
   * @param bizType 业务类型
   * @param bizId 业务id
   * @return
   */
  List<DelayTaskDO> selectListByBizTypeAndBizId(@Param("bizType") int bizType,
                                                @Param("bizId") String bizId);

  /**
   * 查询某个业务在某个时间点后已经超时的任务列表 按业务域区分
   *
   * @param bizType   业务类型
   * @param timePoint 某个时间点
   * @param maxSize   返回记录数的最大值
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithBizTypeAndTimeout(@Param("bizType") int bizType,
                                                    @Param("status") int status,
                                                    @Param("timePoint") Date timePoint,
                                                    @Param("maxSize") int maxSize);

  /**
   * 查询某个业务在某个时间点后消息任务已经超时且没有完成的任务列表
   *
   * @param bizType   业务类型
   * @param timePoint 某个时间点
   * @param maxSize   返回记录数的最大值
   * @return 任务列表
   */
  List<DelayTaskDO> selectListWithBizTypeAndMsgTimeout(@Param("bizType") int bizType,
                                                       @Param("status") int status,
                                                       @Param("msgStatus") int msgStatus,
                                                       @Param("timePoint") Date timePoint,
                                                       @Param("maxSize") int maxSize);

  /**
   * 根据taskId查询任务已经超时的次数
   */
  int selectDelayTimesById(int taskId);

  /**
   * 当
   * @param taskId
   * @param statusTo
   * @param closeReason
   * @param updateTime
   */
  int close(@Param("taskId") int taskId,
            @Param("statusFrom") int statusFrom,
            @Param("statusTo") int statusTo,
            @Param("closeReason") int closeReason,
            @Param("updateTime") Date updateTime);

  int closeMsg(@Param("taskId") int taskId,
               @Param("status") int status,
               @Param("msgStatus") int msgStatus,
               @Param("updateTime") Date updateTime);

  int updateOnRetry(@Param("taskId") int taskId,
                    @Param("status") int status,
                    @Param("delayTimeIncrement") int delayTimeIncrement,
                    @Param("updateTime") Date updateTime);

  int updateMsgOnRetry(@Param("taskId") int taskId,
                       @Param("status") int status,
                       @Param("delayTimeIncrement") int delayTimeIncrement,
                       @Param("updateTime") Date updateTime);

  int updateSuspendTime(@Param("taskId") int taskId,
                        @Param("statusFrom") int statusFrom,
                        @Param("statusTo") int statusTo,
                        @Param("suspendTime") Date suspendTime,
                        @Param("updateTime") Date updateTime);

  /**
   * @param delayMsgEndTime 如果没有消息任务，该字段可能为空
   */
  int updateStatusAndEndTime(@Param("taskId") int taskId,
                             @Param("status") int status,
                             @Param("delayEndTime") Date delayTaskEndTime,
                             @Param("msgEndTime") Date delayMsgEndTime,
                             @Param("updateTime") Date updateTime);

  int closeTaskAhead(@Param("bizType") int bizType,
                     @Param("bizId") String bizId,
                     @Param("statusFrom") int statusFrom,
                     @Param("statusTo") int statusTo,
                     @Param("closeReason") int closeReason,
                     @Param("updateTime") Date updateTime);

  /**
   * 增加delay_end_time
   *
   * @param bizType 业务类型
   * @param bizId 业务ID
   * @param status 任务状态
   * @param delayTimeIncrement delay_end_time的增加量,以秒为单位
   * @param updateTime 更新时间
   * @return 受影响的行数
   */
  int updateDelayEndTime(@Param("bizType") int bizType,
                         @Param("bizId") String bizId,
                         @Param("status") int status,
                         @Param("delayTimeIncrement") int delayTimeIncrement,
                         @Param("updateTime") Date updateTime);

  /**
   * 根据任务ID, 对于待处理的任务, 锁定任务表示开始处理
   *
   * @param taskId 任务ID
   * @param status 任务状态
   * @param currentTime 当前时间
   * @return 受影响的行数
   */
  int tryLockByTaskId(@Param("taskId") int taskId,
                      @Param("status") int status,
                      @Param("currentTime") Date currentTime);

  /**
   * 根据任务ID, 对于待处理的任务, 强制锁定
   *
   * @param taskId 任务ID
   * @param status 任务状态
   * @param internalMinutes 间隔时间,单位是分钟
   * @param currentTime 当前时间
   * @return 受影响的行数
   */
  int forceLockByTaskId(@Param("taskId") int taskId,
                        @Param("status") int status,
                        @Param("internalMinutes") int internalMinutes,
                        @Param("currentTime") Date currentTime);

  /**
   * 根据任务ID, 解锁任务
   *
   * @param taskId 任务ID
   * @param updateTime 更新时间
   * @return 受影响的行数
   */
  int unlockByTaskId(@Param("taskId") int taskId,
                     @Param("updateTime") Date updateTime);

  /**
   * 根据任务ID, 对于处于活跃状态的消息任务, 锁定消息任务表示开始处理
   *
   * @param taskId 任务ID
   * @param status 任务状态
   * @param currentTime 当前时间
   * @return 受影响的行数
   */
  int tryLockMsgByTaskId(@Param("taskId") int taskId,
                         @Param("status") int status,
                         @Param("currentTime") Date currentTime);

  /**
   * 根据任务ID, 对于待处理的消息任务, 强制锁定
   *
   * @param taskId 任务ID
   * @param status 任务状态
   * @param internalMinutes 间隔时间,单位是分钟
   * @param currentTime 当前时间
   * @return 受影响的行数
   */
  int forceLockMsgByTaskId(@Param("taskId") int taskId,
                           @Param("status") int status,
                           @Param("internalMinutes") int internalMinutes,
                           @Param("currentTime") Date currentTime);

  /**
   * 根据任务ID, 解锁消息任务
   *
   * @param taskId 任务ID
   * @param updateTime 更新时间
   * @return 受影响的行数
   */
  int unlockMsgByTaskId(@Param("taskId") int taskId,
                        @Param("updateTime") Date updateTime);
}
