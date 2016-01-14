package com.youzan.trade.timeout.api.order.delivered;

import com.youzan.api.common.response.PlainResult;
import com.youzan.trade.timeout.api.order.delivered.model.DelayParams;
import com.youzan.trade.timeout.api.order.delivered.model.IncreaseDelayTimeRequestParams;

/**
 * 针对订单已发货业务, 对外提供服务
 *
 * @author apple created at: 16/1/14 上午11:12
 */
public interface OrderDeliveredDelayTaskService {

  /**
   * 对订单已发货的延时任务, 延长到期时间
   *
   * @param delayParams 延时任务调用参数
   * @return 操作执行结果
   */
  PlainResult<Integer> increaseDelayEndTime(IncreaseDelayTimeRequestParams delayParams);

  /**
   * 确认收货，将相关任务关闭
   */
  PlainResult<Boolean> finishDelayTask(DelayParams delayParams);

  /**
   * 获取订单最终发货时间
   * @param delayParams
   * @return
   */
  PlainResult<Integer> getEndTime(DelayParams delayParams);
}
