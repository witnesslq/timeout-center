package com.youzan.trade.timeout.api.order.delivered.model;

import java.io.Serializable;

import lombok.Data;

/**
 * 由于目前dubbo对于接口解析有参数顺序的要求
 *
 * 不包含bizType, 因为不应该让调用方感知到各自业务在超时中心对应的bizType
 *
 * @author apple created at: 16/1/14 下午12:15
 */
@Data
public class DelayParams implements Serializable {

  private static final long serialVersionUID = 7552235130865273546L;

  private String bizId;

  private Integer bizShardKey;

}
