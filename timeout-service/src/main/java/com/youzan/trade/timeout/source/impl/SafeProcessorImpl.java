package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.BizType;
import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.SafeType;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.service.DelayTaskService;
import com.youzan.trade.timeout.source.Processor;

import com.alibaba.fastjson.JSON;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;

/**
 * 维权消息处理器
 *
 * @author apple created at: 15/10/28 上午10:02
 */
@Component
public class SafeProcessorImpl implements Processor {

  @Resource
  private DelayTaskService delayTaskService;

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

    // 我要退款，但不退货
    if (SafeType.REFUND_ONLY.code() == safe.getSafeType()) {

      // 201
      if (SafeState.BUYER_START.code() == safe.getState()) {
        return startTask(safe);
      }

      // 203 + 202
      if (SafeState.SELLER_REJECTED.code() == safe.getState()
          || SafeState.BUYER_RESTART.code() == safe.getState()) {
        return restartTask(safe);
      }

      // 205 + 204 + 249 + 250
      if (SafeState.SELLER_ACCEPTED.code() == safe.getState()
          || SafeState.INVOLVED.code() == safe.getState()
          || SafeState.CLOSED.code()   == safe.getState()
          || SafeState.FINISHED.code() == safe.getState()) {
        return closeAllTasks(safe);
      }
    }

    if (SafeType.REFUND_RETURN.code() == safe.getSafeType()) {

      // 201
      if (SafeState.BUYER_START.code() == safe.getState()) {
        return startTask(safe);
      }

      // 205 + 203 + 202 + 206 + 207
      if (SafeState.SELLER_ACCEPTED.code() == safe.getState()
          || SafeState.SELLER_REJECTED.code() == safe.getState()
          || SafeState.BUYER_RESTART.code() == safe.getState()
          || SafeState.BUYER_SENT.code() == safe.getState()
          || SafeState.SELLER_NOT_RECEIVED.code() == safe.getState()) {
        return restartTask(safe);
      }

      // 204 + 249 + 250
      if (SafeState.INVOLVED.code() == safe.getState()
          || SafeState.CLOSED.code() == safe.getState()
          || SafeState.FINISHED.code() == safe.getState()) {
        return closeAllTasks(safe);
      }
    }

    return false;
  }

  private boolean startTask(Safe safe) {
    return delayTaskService.saveBySafe(safe);
  }

  private boolean closeAllTasks(Safe safe) {
    return delayTaskService.closeTaskByBizTypeAndBizId(BizType.SAFE.code(), safe.getSafeNo());
  }

  private boolean restartTask(Safe safe) {
    return defaultTxTemplate.execute(transactionStatus -> {
      if ( !( closeAllTasks(safe) && startTask(safe) ) ) {
        transactionStatus.setRollbackOnly();
        return false;
      }

      return true;
    });
  }

}
