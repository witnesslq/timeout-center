package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.service.SafeService;
import com.youzan.trade.util.LogUtils;

import com.google.common.collect.Maps;

import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
@Slf4j
@Component(value = "safeServiceImpl")
public class SafeServiceImpl implements SafeService {

  @Override
  public Boolean checkOrderFeedbackFinish(String orderNo, Integer kdtId) {
    Map<String, Object> params = Maps.newHashMap();
    params.put("order_no", orderNo);
    params.put("kdt_id", kdtId);
    BaseResult<Boolean>
        result =
        Client.call("trade.safe.modify.checkOrderFeedbackFinish", params, Boolean.TRUE);
    if (result == null || ResponseCode.SUCC != result.getCode()) {
      LogUtils.error(log, "Check order feedback status failed.orderNo={}", orderNo);
      return false;
    }

    return result.getData();
  }
}
