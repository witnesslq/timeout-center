package com.youzan.trade.timeout.model;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;

/**
 * @author apple created at: 15/11/1 上午11:52
 */
@Data
public class MsgTaskResult {

  @JsonProperty("ret_code")
  private Integer resultCode;

  @JsonProperty("ret_msg")
  private String resultMessage;

}
