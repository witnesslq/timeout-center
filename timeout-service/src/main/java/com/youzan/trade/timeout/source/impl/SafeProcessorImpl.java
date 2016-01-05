package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.CloseReason;
import com.youzan.trade.timeout.constants.Constants;
import com.youzan.trade.timeout.constants.DelayState;
import com.youzan.trade.timeout.constants.MsgStatus;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.SafeType;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.DelayTask;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.service.DelayTimeStrategy;
import com.youzan.trade.timeout.source.Processor;
import com.youzan.trade.util.LogUtils;
import com.youzan.trade.util.TimeUtils;

import com.alibaba.fastjson.JSON;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

/**
 * 维权消息处理器
 *
 * @author apple created at: 15/10/28 上午10:02
 */
@Slf4j
@Component("safeProcessorImpl")
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
      LogUtils.error(log, "Invalid safe message={}", message);
      return true;
    }

    // 过滤不需要的维权任务
    if (safe.getDelayState() == null ||
        safe.getDelayState() == DelayState.NOTNEEDED.code()) {
      return true;
    }

    if (SafeType.REFUND_ONLY.code() == safe.getSafeType()) {
      switch (SafeState.getSafeStateByCode(safe.getState())) {
        case BUYER_START          : return processOnStart(safe);          // 201
        case BUYER_RESTART        : return processOnRestart(safe);        // 202
        case SELLER_REJECTED      : return processOnRestart(safe);        // 203
        case INVOLVED             : return processOnClose(safe);          // 204
        case SELLER_ACCEPTED      : return processOnClose(safe);          // 205
        case CLOSED               : return processOnClose(safe);          // 249
        case FINISHED             : return processOnClose(safe);          // 250

      }
    } else if (SafeType.REFUND_RETURN.code() == safe.getSafeType()) {
      switch (SafeState.getSafeStateByCode(safe.getState())) {
        case BUYER_START          : return processOnStart(safe);          // 201
        case BUYER_RESTART        : return processOnRestart(safe);        // 202
        case SELLER_REJECTED      : return processOnRestart(safe);        // 203
        case INVOLVED             : return processOnClose(safe);          // 204
        case SELLER_ACCEPTED      : return processOnRestart(safe);        // 205
        case BUYER_SENT           : return processOnRestart(safe);        // 206
        case SELLER_NOT_RECEIVED  : return processOnRestart(safe);        // 207
        case CLOSED               : return processOnClose(safe);          // 249
        case FINISHED             : return processOnClose(safe);          // 250
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
    delayTask.setDelayEndTime(calDelayEndTime(safe));
    delayTask.setDelayTimes(Constants.INITIAL_DELAY_TIMES);

    if (safe.isNeedMsg()) {
      delayTask.setMsgStatus(MsgStatus.ACTIVE.code());
      delayTask.setMsgEndTime(calMsgEndTime(safe));
    } else {
      delayTask.setMsgStatus(MsgStatus.NONE.code());
      delayTask.setMsgEndTime(TimeUtils.getDateBySeconds(Constants.INITIAL_MSG_END_TIME));
    }

    delayTask.setCreateTime(TimeUtils.getDateBySeconds(TimeUtils.currentInSeconds()));

    return delayTaskService.save(delayTask);
  }

  private Date calDelayEndTime(Safe safe) {
    return TimeUtils.getDateBySeconds(safe.getRecordTime() + delayTimeStrategy
        .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState()));
  }

  private Date calMsgEndTime(Safe safe) {
    return TimeUtils.getDateBySeconds(safe.getRecordTime() + msgDelayTimeStrategy
        .getInitialDelayTime(BizType.SAFE.code(), safe.getSafeNo(), safe.getState()));
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
