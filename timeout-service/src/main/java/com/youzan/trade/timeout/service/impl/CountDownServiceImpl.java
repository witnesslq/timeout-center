package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.common.httpclient.constant.ResponseCode;
import com.youzan.trade.timeout.service.CountDownService;
import com.youzan.trade.util.LogUtils;

import com.google.common.collect.Maps;

import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;


/**
 * @author Created by liwenjia@youzan.com on 2016/01/18 .
 */
@Slf4j
@Component
public class CountDownServiceImpl implements CountDownService {

  @Override
  public boolean shouldSendMsg(String orderNo, Integer kdtId) {
    Map<String, Object> params = Maps.newHashMap();
    params.put("order_no", orderNo);
    params.put("kdt_id", kdtId);
    BaseResult<Boolean>
        result =
        Client.call("trade.order.countDown.isAllowLaterReceive", params, Boolean.TRUE);
    if (result != null) {
      if (result.getCode() == ResponseCode.SUCC) {
        return result.getData();
      }
      LogUtils.error(log, "[ShouldSendMsg]Failed.orderNo={}.preExcep={},msg={}", orderNo,
                     result.getPreException() + " " + result.getMsg());
    } else {
      LogUtils.error(log, "[ShouldSendMsg]Failed.orderNo={}.result=null", orderNo);
    }
    return false;
  }
}
