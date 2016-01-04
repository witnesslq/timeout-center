package com.youzan.trade.timeout.order.service.impl;

import com.youzan.platform.util.json.JsonUtil;
import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.OrderState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.Order;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.order.service.DeliveredOrderService;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.impl.AbstractOrderRelatedDelayTimeStrategy;
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

  @Resource(name = "autoCompleteTaskDelayTimeStrategyImpl")
  private AbstractOrderRelatedDelayTimeStrategy delayTimeStrategy;

  @Resource(name = "autoCompleteTaskMsgDelayTaskTimeStrategyImpl")
  private AbstractOrderRelatedDelayTimeStrategy msgDelayTimeStrategy;

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
      DelayTask task = delayTaskService.getTaskByBizIdAndBizType(order.getOrderNo(),
                                                                 BizType.DELIVERED_ORDER.code());
      if (task != null) {
        LogUtils.warn(log, "[DeliveredOrderTask]Already added.order={},type={}", order.getOrderNo(),
                      BizType.DELIVERED_ORDER.code());
        return true;
      }

      try {
        delayTaskService.save(buildDeliveredOrderTask(order));
      } catch (Exception e) {
        LogUtils
            .error(log, "[DeliveredOrderTask]Save task failed.orderNo={}", order.getOrderNo(), e);
      }
    } else {
      LogUtils.warn(log, "[DeliveredOrderTask]Abnormal order state={}.orderNo={}",
                    order.getOrderState(), order.getOrderNo());
    }
    return true;
  }

  /**
   * 对于已完成或已关闭订单，
   * @param order
   * @return
   */
  private boolean checkDeliveredOrder(Order order) {
    if (null == order) {
      return false;
    }

    //TODO Get order from db.

    if(!Objects.equals(OrderState.SENT.getState(), order.getOrderState())){
      return false;
    }

    if (order.getExpressTime() == null || order.getExpressTime() <= 0) {
      return false;
    }
    return true;
  }

  /**
   * @param order
   * @return
   */
  private DelayTask buildDeliveredOrderTask(Order order) {
    DelayTask task = new DelayTask();
    task.setBizType(BizType.DELIVERED_ORDER.code());
    task.setBizId(order.getOrderNo());
    task.setBizState(order.getOrderState());
    task.setStatus(TaskStatus.ACTIVE.code());
    task.setCloseReason(CloseReason.NOT_CLOSED.code());
    Date current = TimeUtils.currentDate();
    task.setCreateTime(current);
    task.setDelayStartTime(TimeUtils.getDateBySeconds(order.getExpressTime()));

    Date msgEndTime = calMsgEndTime(order);

    if (msgEndTime != null) {
      //默认是要发送通知的
      task.setMsgEndTime(msgEndTime);
      task.setMsgStatus(MsgStatus.ACTIVE.code());
    } else {
      task.setMsgEndTime(new Date(0));//set default time
      task.setMsgStatus(MsgStatus.NONE.code());
    }

    task.setDelayEndTime(calDelayEndTime(order));
    task.setDelayTimes(Constants.INITIAL_DELAY_TIMES);

    return task;
  }

  protected Date calDelayEndTime(Order order) {
    int endTime = order.getExpressTime() + delayTimeStrategy
        .getInitialDelayTimeByOrderType(
            BizType.DELIVERED_ORDER.code(), order.getOrderType());
    return TimeUtils.getDateBySeconds(endTime);
  }

  protected Date calMsgEndTime(Order order) {
    int endTime = order.getExpressTime() + msgDelayTimeStrategy
        .getInitialDelayTimeByOrderType(
            BizType.DELIVERED_ORDER.code(), order.getOrderType());
    if (endTime < TimeUtils.currentInSeconds()) {
      return null;
    }
    return TimeUtils.getDateBySeconds(endTime);
  }

  protected boolean isSent(Order order) {
    return Objects.equals(OrderState.SENT.getState(), order.getOrderState());
  }
}
