package com.youzan.trade.timeout.handler.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.handler.AbstractMsgTaskHandler;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.TaskResult;
import com.youzan.trade.util.LogUtils;

import com.google.common.collect.Maps;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author apple created at: 16/1/4 上午12:29
 */
@Slf4j
@Component("orderDeliveredMsgTaskHandlerImpl")
public class OrderDeliveredMsgTaskHandlerImpl extends AbstractMsgTaskHandler {

  private String callPath = "trade.order.countDown.smsAlert";

  @Async("defaultThreadPoolTaskExecutor")
  @Override
  public void handle(DelayTask delayTask) {
    /**
     * 先尝试获取锁
     * 如果获取不到,则什么都不做
     */
    if (!delayTaskService.lockMsgTaskByTaskId(delayTask.getId())) {
      LogUtils.info(log, "处理消息任务, 获取消息任务锁失败, taskId: {}", delayTask.getId());
      return;
    }

    try {
      Map<String, Object> params = Maps.newHashMap();
      generateParamsByDelayTask(delayTask, params);
    /*
    BaseResult<TaskResult> result = Client.call(getCallPath(),
                                                params,
                                                new TaskResult());
                                                */
      //start
      BaseResult<TaskResult> result = new BaseResult<>();
      result.setCode(ResponseCode.SUCC);
      TaskResult data = new TaskResult();
      data.setResultCode(Constants.MSG_TASK_SUCCESS);
      result.setData(data);
      //end

      if (handleDelayTaskByResponseCode(delayTask, result.getCode())) {
        return;
      }

      handleDelayTaskByResultCode(delayTask, result.getData().getResultCode());
    } finally {
      /**
       * 最后释放锁
       */
      delayTaskService.unlockMsgTaskByTaskId(delayTask.getId());
    }
  }

  private void generateParamsByDelayTask(DelayTask delayTask, Map<String, Object> params) {
    params.put("order_no", delayTask.getBizId());
    params.put("kdt_id", delayTask.getBizShardKey());
  }

  @Override
  protected String getCallPath() {
    return this.callPath;
  }
}
