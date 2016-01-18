package com.youzan.trade.timeout.api.order.delivered.model;

import lombok.Data;

/**
 * 由于目前dubbo对于接口解析有参数顺序的要求
 *
 * 不包含bizType, 因为不应该让调用方感知到各自业务在超时中心对应的bizType
 *
 * @author apple created at: 16/1/14 下午12:15
 */
@Data
public class IncreaseDelayTimeRequestParams extends DelayParams {

  /***/
  private Integer delayPeriod;

}
