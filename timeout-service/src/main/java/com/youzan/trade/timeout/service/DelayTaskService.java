package com.youzan.trade.timeout.service;

import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;

import java.util.List;

/**
 * @author apple created at: 15/10/23 上午11:53
 */
public interface DelayTaskService {

  /**
   * 根据一条维权记录保存一个超时任务
   *
   * @param safe
   * @return
   */
  boolean saveBySafe(Safe safe);

  /**
   * 获取某个时间点已经超时且没有完成的任务
   *
   * @return
   */
  List<DelayTask> getListWithTimeout(int timePoint);

  /**
   * 获取当前已经超时的任务
   *
   * @return
   */
  List<DelayTask> getListWithTimeoutCurrently();

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
   * 根据业务类型和业务id关闭延时任务
   *
   * @param bizType
   * @param bizId
   * @return
   */
  boolean closeTaskAhead(int bizType, String bizId);
}
