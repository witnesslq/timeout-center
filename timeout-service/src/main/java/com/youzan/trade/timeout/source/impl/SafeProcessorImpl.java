package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.constants.DelayState;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.SafeType;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.dal.dataobject.DelayTaskDO;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.TimeUtils;

import com.alibaba.fastjson.JSON;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 维权消息处理器
 *
 * @author apple created at: 15/10/28 上午10:02
 */
@Component(value = "safeProcessorImpl")
public class SafeProcessorImpl implements Processor {

  @Resource
  private DelayTaskService delayTaskService;

  @Resource(name = "delayTimeStrategyImpl")
  private DelayTimeStrategy delayTimeStrategy;

  @Resource(name = "msgDelayTimeStrategyImpl")
  private DelayTimeStrategy msgDelayTimeStrategy;

  @Resource
  private TransactionTemplate defaultTxTemplate;

  @Override
  public boolean process(String message) {
    if (message == null) {
      return true;
    }

    Safe safe = JSON.parseObject(message, Safe.class);

    if (safe == null) {
      return true;
    }

    if (safe.getDelayState() == null ||
        safe.getDelayState() == DelayState.NOTNEEDED.code()) {
      return true;
    }

    // 我要退款，但不退货
    if (SafeType.REFUND_ONLY.code() == safe.getSafeType()) {

      // 201
      if (SafeState.BUYER_START.code() == safe.getState()) {
        return processOnStart(safe);
      }

      // 203 + 202
      if (SafeState.SELLER_REJECTED.code() == safe.getState()
          || SafeState.BUYER_RESTART.code() == safe.getState()) {
        return processOnRestart(safe);
      }

      // 205 + 204 + 249 + 250
      if (SafeState.SELLER_ACCEPTED.code() == safe.getState()
          || SafeState.INVOLVED.code() == safe.getState()
          || SafeState.CLOSED.code()   == safe.getState()
          || SafeState.FINISHED.code() == safe.getState()) {
        return processOnClose(safe);
      }
    }

    if (SafeType.REFUND_RETURN.code() == safe.getSafeType()) {

      // 201
      if (SafeState.BUYER_START.code() == safe.getState()) {
        return processOnStart(safe);
      }

      // 205 + 203 + 202 + 206 + 207
      if (SafeState.SELLER_ACCEPTED.code() == safe.getState()
          || SafeState.SELLER_REJECTED.code() == safe.getState()
          || SafeState.BUYER_RESTART.code() == safe.getState()
          || SafeState.BUYER_SENT.code() == safe.getState()
          || SafeState.SELLER_NOT_RECEIVED.code() == safe.getState()) {
        return processOnRestart(safe);
      }

      // 204 + 249 + 250
      if (SafeState.INVOLVED.code() == safe.getState()
          || SafeState.CLOSED.code() == safe.getState()
          || SafeState.FINISHED.code() == safe.getState()) {
        return processOnClose(safe);
      }
    }

    return false;
  }

  private boolean processOnStart(Safe safe) {
    DelayTask delayTask = new DelayTask();
    delayTask.setBizType(BizType.SAFE.code());
    delayTask.setBizId(safe.getSafeNo());
    delayTask.setBizState(safe.getState());
    delayTask.setStatus(TaskStatus.ACTIVE.code());
    delayTask.setCloseReason(CloseReason.NOT_CLOSED.code());
    delayTask.setDelayStartTime(TimeUtils.getDateBySeconds(safe.getRecordTime()));
    delayTask.setDelayEndTime(TimeUtils.getDateBySeconds(safe.getRecordTime() + delayTimeStrategy
        .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState())));
    delayTask.setDelayTimes(Constants.INITIAL_DELAY_TIMES);

    if (safe.isNeedMsg()) {
      delayTask.setMsgStatus(MsgStatus.ACTIVE.code());
      delayTask.setMsgEndTime(TimeUtils.getDateBySeconds(safe.getRecordTime() + msgDelayTimeStrategy
          .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState())));
    } else {
      delayTask.setMsgStatus(MsgStatus.NONE.code());
      delayTask.setMsgEndTime(TimeUtils.getDateBySeconds(Constants.INITIAL_MSG_END_TIME));
    }

    delayTask.setCreateTime(TimeUtils.getDateBySeconds(TimeUtils.currentInSeconds()));

    return delayTaskService.save(delayTask);
  }

  private boolean processOnClose(Safe safe) {
    return delayTaskService.closeTaskByBizTypeAndBizId(BizType.SAFE.code(), safe.getSafeNo());
  }

  private boolean processOnRestart(Safe safe) {
    return defaultTxTemplate.execute(transactionStatus -> {
      if ( !( processOnClose(safe) && processOnStart(safe) ) ) {
        transactionStatus.setRollbackOnly();
        return false;
      }

      return true;
    });
  }

}
