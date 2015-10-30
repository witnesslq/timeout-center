package com.youzan.trade.timeout.model;

import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.SafeType;

import com.alibaba.fastjson.annotation.JSONField;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Data;

/**
 * @author apple created at: 15/10/27 下午5:41
 */
@Data
public class Safe {

  /** 投诉编号 */
  @JSONField(name = "safe_no")
  private String safeNo;

  /** 添加时间 */
  @JSONField(name = "add_time")
  private Integer addTime;

  /** 更新时间 */
  @JSONField(name = "update_time")
  private Integer updateTime;

  /** 退款类型 */
  @JSONField(name = "safe_type")
  private Integer safeType;

  /** 维权状态 */
  @JSONField(name = "state")
  private Integer state;

  public Integer getRecordTime() {
    if (state == null) {
      return null;
    }

    return SafeState.BUYER_START.code() == state ? addTime : updateTime;
  }

  public boolean isNeedMsg() {
    if (safeType == null || state == null) {
      return false;
    }

    if (safeType == SafeType.REFUND_ONLY.code() && state == SafeState.BUYER_START.code()) {
      return true;
    }

    if (safeType == SafeType.REFUND_RETURN.code() && state == SafeState.BUYER_SENT.code()) {
      return true;
    }

    return false;
  }


}
