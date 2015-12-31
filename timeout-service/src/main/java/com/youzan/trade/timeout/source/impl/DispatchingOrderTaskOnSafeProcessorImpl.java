package com.youzan.trade.timeout.source.impl;

import com.youzan.platform.util.json.JsonUtil;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 监听维权消息，区分维权出于那种状态，来对deliveredOrderTask进行中断/恢复处理
 *
 * @author liwenjia created at: 15/12/31
 */
@Component(value = "dispatchingOrderTaskOnSafeProcessorImpl")
@Slf4j
public class DispatchingOrderTaskOnSafeProcessorImpl implements Processor {

  @Resource
  DeliveredOrderService deliveredOrderService;

  @Resource
  DelayTaskService delayTaskService;

  @Override
  public boolean process(String message) {
    if (StringUtils.isBlank(message)) {
      LogUtils.warn(log, "Message's blank");
      return true;
    }

    Safe safe;
    try {
      safe = JsonUtil.json2obj(message, Safe.class);
    } catch (IOException e) {
      LogUtils.error(log, "Parse message failed.message=" + message, e);
      return true;
    }
    String orderNo = safe.getOrderNo();
    DelayTask
        orderTask =
        delayTaskService.getTaskByBizIdAndBizType(orderNo, BizType.DELIVERED_ORDER.code());

    if (orderTask == null) {
      LogUtils.info(log, "[Pass]OrderTask not found.safeNo={}", safe.getSafeNo());
      return true;
    }

    TaskStatus expectedStatus = inferOrderTaskStatusOnSafe(safe);

    if (expectedStatus == null) {
      return true;
    }

    switch (expectedStatus) {
      case ACTIVE:
        //恢复
        return delayTaskService.resumeTask(orderTask);
      case SUSPENDED:
        //中断
        return delayTaskService.suspendTask(orderTask);
      case CLOSED:
        //关闭
        LogUtils.info(log, "超时任务提前关闭", orderTask.getId());
        return delayTaskService.closeOnNoRetry(orderTask.getId());
      default:
        LogUtils.warn(log, "No need to process orderTask.safeNo={}", safe.getSafeNo());
        return true;
    }
  }

  /**
   * 根据维权记录当前状态来判断orderTask可以处于的状态 TODO
   */
  private TaskStatus inferOrderTaskStatusOnSafe(Safe safe) {
    //获取orderTask 若orderTask不存在，则直接返回null

    //哪些状态可以视为中断
    //哪些状态可以视为恢复
    return null;
  }

}
