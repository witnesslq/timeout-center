package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;

import java.util.Date;
import java.util.List;

/**
 * @author apple created at: 15/10/23 上午11:53
 */
public interface DelayTaskService {

  /**
   * 保存一个超时任务
   *
   * @param delayTask
   * @return
   */
  boolean save(DelayTask delayTask);

  /**
   * 获取某个时间点已经超时且没有完成的任务
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTask> getListWithTimeout(Date timePoint);

  /**
   * 获取当前已经超时的任务
   *
   * @return 任务列表
   */
  List<DelayTask> getListWithTimeoutCurrently();

  /**
   * 获取某个业务在某个时间点后已经超时的任务列表
   * 按业务域区分
   *
   * @param bizType 业务类型
   * @param timePoint 某个时间点
   * @param maxSize 返回任务数的最大值
   * @return 任务列表
   */
  List<DelayTask> getListWithBizTypeAndTimeout(int bizType, Date timePoint, int maxSize);

  /**
   * 获取某个业务当前已经超时的任务列表
   * 按业务域区分
   *
   * @param bizType 业务类型
   * @param maxSize 返回任务数的最大值
   * @return 任务列表
   */
  List<DelayTask> getListWithBizTypeAndTimeoutCurrently(int bizType, int maxSize);

  /**
   * 获取某个时间点消息任务已经超时且没有完成的任务
   *
   * @param timePoint 某个时间点
   * @return 任务列表
   */
  List<DelayTask> getListWithMsgTimeout(Date timePoint);

  /**
   * 获取当前时间消息任务已经超时且没有完成的任务
   *
   * @return 任务列表
   */
  List<DelayTask> getListWithMsgTimeoutCurrently();

  /**
   * 获取某个业务在某个时间点后消息任务已经超时且没有完成的任务列表
   * 按业务域区分
   *
   * @param bizType 业务类型
   * @param timePoint 某个时间点
   * @param maxSize 返回任务数的最大值
   * @return 任务列表
   */
  List<DelayTask> getListWithBizTypeAndMsgTimeout(int bizType, Date timePoint, int maxSize);

  /**
   * 获取某个业务当前消息任务已经超时且没有完成的任务列表
   * 按业务域区分
   *
   * @param bizType 业务类型
   * @param maxSize 返回任务数的最大值
   * @return 任务列表
   */
  List<DelayTask> getListWithBizTypeAndMsgTimeoutCurrently(int bizType, int maxSize);

  /**
   * 执行成功, 关闭超时任务
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean closeOnSuccess(int taskId);

  /**
   * 执行失败, 关闭超时任务, 不再重试
   * 比如因为state值已经发生变化
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean closeOnNoRetry(int taskId);

  /**
   * 执行失败, 更新超时任务
   * 延续任务
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean updateOnRetry(int taskId);

  /**
   * 消息任务执行成功并关闭
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean closeMsgOnSuccess(int taskId);

  /**
   * 执行消息任务失败, 关闭消息任务, 不再重试
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean closeMsgOnNoRetry(int taskId);


  /**
   * 执行消息任务失败, 更新消息任务
   * 延续消息任务
   *
   * @param taskId 超时任务唯一标识
   * @return
   */
  boolean updateMsgOnRetry(int taskId);

  /**
   * 根据业务类型和业务id关闭延时任务
   *
   * @param bizType
   * @param bizId
   * @return
   */
  boolean closeTaskByBizTypeAndBizId(int bizType, String bizId);

  /**
   * 根据业务类型和业务id获取唯一任务
   *
   * @param bizType 业务类型
   * @param bizId 业务id
   * @return 延时任务
   */
  DelayTask getTaskByBizTypeAndBizId(int bizType, String bizId);

  /**
   * 中断任务
   *
   * @param task 具体任务
   * @return
   */
  boolean suspendTask(DelayTask task);

  /**
   * 恢复任务
   *
   * @param task 具体任务
   * @param suspendTime
   * @return
   */
  boolean resumeTask(DelayTask task, long suspendTime);

  /**
   * 延长任务时间
   *
   * @param task 具体任务
   * @param expendTime 延长时间
   * @return
   */
  boolean enlargeTask(DelayTask task, int expendTime);

}
