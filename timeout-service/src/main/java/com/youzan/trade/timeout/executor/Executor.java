package com.youzan.trade.timeout.executor;

import com.youzan.trade.timeout.handler.TaskHandler;

/**
 * @author apple created at: 15/10/23 下午8:16
 */
public interface Executor {
  void execute(int lockId, TaskHandler taskHandler);
}
