package com.youzan.trade.timeout.model;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;

/**
 * @author apple created at: 15/10/29 下午3:54
 */
@Data
public class SafeTaskResult {

  @JsonProperty("ret_code")
  private Integer resultCode;

  @JsonProperty("ret_msg")
  private String resultMessage;

}
