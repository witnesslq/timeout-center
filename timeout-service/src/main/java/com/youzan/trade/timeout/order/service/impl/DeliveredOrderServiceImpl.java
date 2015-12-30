package com.youzan.trade.timeout.order.service.impl;

import com.youzan.platform.util.json.JsonUtil;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.OrderState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.entities.Order;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Component(value = "deliveredOrderServiceImpl")
@Slf4j
public class DeliveredOrderServiceImpl implements DeliveredOrderService {

  @Resource(name = "delayTimeStrategyImpl")
  private DelayTimeStrategy delayTimeStrategy;

  @Resource(name = "msgDelayTimeStrategyImpl")
  private DelayTimeStrategy msgDelayTimeStrategy;

  @Resource
  DelayTaskService delayTaskService;

  @Override
  public boolean addToDelayTask(Order order) {
    if (!checkDeliveredOrder(order)) {
      try {
        LogUtils.error(log, "Invalid delivered order.order={}", JsonUtil.obj2json(order));
      } catch (IOException e) {
        LogUtils.error(log, "Invalid delivered order.order={}",
                       order == null ? null : order.getOrderNo(), e);
      }
      return true;
    }

    if (isSent(order)) {
      //获取任务
      DelayTask task = delayTaskService.getTaskByBizIdAndBizType(order.getOrderNo(),
                                                                 BizType.DELIVERED_ORDER.code());
      if (task != null) {
        LogUtils.warn(log, "[DeliveredOrderTask]Already added.order={},type={}", order.getOrderNo(),
                      BizType.DELIVERED_ORDER.code());
        return true;
      }
      //添加延迟任务记录
      task = buildDeliveredOrderTask(order);

      delayTaskService.save(task);
    } else {
      LogUtils.warn(log, "[DeliveredOrderTask]Abnormal order state={}.orderNo={}",
                    order.getOrderState(), order.getOrderNo());
    }
    return true;
  }

  private boolean checkDeliveredOrder(Order order) {
    if (null == order) {
      return false;
    }
    if (order.getExpressTime() == null || order.getExpressTime() <= 0) {
      return false;
    }
    return false;
  }

  /**
   * TODO
   * @param order
   * @return
   */
  private DelayTask buildDeliveredOrderTask(Order order) {
    DelayTask task = new DelayTask();
    task.setBizType(BizType.DELIVERED_ORDER.code());
    task.setBizId(order.getOrderNo());
    task.setBizState(order.getOrderState());
    Date current = TimeUtils.currentDate();
    task.setCreateTime(current);
    task.setDelayEndTime(TimeUtils.getDateBySeconds(calDelayEndTime(order)));
    task.setDelayTimes(Constants.INITIAL_DELAY_TIMES);
    task.setStatus(TaskStatus.ACTIVE.code());
    task.setCloseReason(CloseReason.NOT_CLOSED.code());
    task.setMsgStatus(MsgStatus.ACTIVE.code());
    //默认是要发送通知的
    task.setMsgEndTime(TimeUtils.getDateBySeconds(getMsgEndTimeInSeconds(order)));

    return task;
  }

  private int calDelayEndTime(Order order) {
    return order.getExpressTime() + delayTimeStrategy
        .getInitialDelayTime(
            BizType.DELIVERED_ORDER.code(), order.getOrderNo(), order.getOrderState());
  }

  private int getMsgEndTimeInSeconds(Order order) {
    return order.getExpressTime() + msgDelayTimeStrategy
        .getInitialDelayTime(BizType.DELIVERED_ORDER.code(), order.getOrderNo(),
                             order.getOrderState());
  }

  private boolean isSent(Order order) {
    return Objects.equals(OrderState.SENT.getState(), order.getOrderState());
  }
}
