package com.youzan.trade.timeout.service.impl;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.Client;
import com.youzan.trade.timeout.model.TaskResult;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/04 .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class,
    locations = {"classpath:test-applicationContext.xml"})
public class SafeServiceImplTest extends TestCase {

  @Test
  public void testCheckOrderFeedbackFinish() throws Exception {
    Map<String, Object> params = Maps.newHashMap();
    String orderNo = "E20150913204633090362734";
    Integer kdtId = 1;
    params.put("order_no", orderNo);
    params.put("kdt_id", kdtId);
    BaseResult<Boolean>
        result =
        Client.call("trade.safe.modify.checkOrderFeedbackFinish", params, Boolean.TRUE);


  }

  @Test
  public void test(){
    String safeNo = "W1511051651468166";
    Integer safeState = 205;
    Map<String, Object> params = Maps.newHashMap();
    params.put("safe_no", safeNo);
    params.put("state", safeState);

    BaseResult<TaskResult> result = Client.call("trade.safe.timeout.sendMsg",
                                                params,
                                                new TaskResult());
  }

  @Test
  public void countDownTest(){
    Map<String, Object> params = Maps.newHashMap();
    String orderNo = "E1";
    Integer kdtId =1;
    params.put("order_no", orderNo);
    params.put("kdt_id", kdtId);
    BaseResult<Boolean>
        result =
        Client.call("trade.order.countDown.isAllowLaterReceive", params, Boolean.TRUE);

  }
}
