package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.constants.TaskStatus;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.service.SafeService;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import javax.annotation.Resource;

import static org.mockito.Mockito.when;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/03 .
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoContextLoader.class,
    locations = {"classpath:test-applicationContext.xml", "classpath:test-mock-safe.xml"})
public class DispatchingOrderTaskOnSafeProcessorImplTest extends TestCase {

  @Resource
  @InjectMocks
  DispatchingOrderTaskOnSafeProcessorImpl dispatchingOrderTaskOnSafeProcessor;

  @Mock
  SafeService safeService;

  @Before
  public void init(){
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testInferOrderTaskStatusOnSafeOrderUnfree() throws Exception {
    //TODO
    String orderNo = "E123";
    Integer kdtId= 1;
    Safe safe = new Safe();
    safe.setOrderNo(orderNo);
    safe.setKdtId(kdtId);
    when(safeService.checkOrderFeedbackFinish(orderNo, kdtId)).thenReturn(Boolean.FALSE);
    Map<Integer,TaskStatus> datas = inferOrderTaskStatusOnSafeTestDataWithOrderSafeUnFree();
    for(Map.Entry<Integer,TaskStatus> entry:datas.entrySet()){
      safe.setState(entry.getKey());
      assertEquals(entry.getValue(),
                   dispatchingOrderTaskOnSafeProcessor.inferOrderTaskStatusOnSafe(safe));
    }
  }


  @Test
  public void testInferOrderTaskStatusOnSafeOrderfree() throws Exception {
    //TODO
    String orderNo = "E123";
    Integer kdtId= 1;
    Safe safe = new Safe();
    safe.setOrderNo(orderNo);
    safe.setKdtId(kdtId);
    when(safeService.checkOrderFeedbackFinish(orderNo, kdtId)).thenReturn(Boolean.TRUE);
    Map<Integer,TaskStatus> datas = inferOrderTaskStatusOnSafeTestDataWithOrderSafeFree();
    for(Map.Entry<Integer,TaskStatus> entry:datas.entrySet()){
      safe.setState(entry.getKey());
      assertEquals(entry.getValue(),dispatchingOrderTaskOnSafeProcessor.inferOrderTaskStatusOnSafe(safe));
    }
  }

  public Map<Integer,TaskStatus> inferOrderTaskStatusOnSafeTestDataWithOrderSafeFree(){
    Map<Integer,TaskStatus> datas = Maps.newHashMap();
    //没有其他维权进行中
    datas.put(SafeState.BUYER_START.code(),TaskStatus.SUSPENDED);
    datas.put(SafeState.CLOSED.code(),TaskStatus.ACTIVE);
    datas.put(SafeState.FINISHED.code(),TaskStatus.ACTIVE);
    datas.put(SafeState.BUYER_RESTART.code(),null);
    datas.put(SafeState.BUYER_SENT.code(),null);
    datas.put(SafeState.INVOLVED.code(),null);
    datas.put(SafeState.SELLER_ACCEPTED.code(),null);
    datas.put(SafeState.SELLER_NOT_RECEIVED.code(),null);
    datas.put(SafeState.SELLER_REJECTED.code(),null);
    return datas;
  }

  public Map<Integer,TaskStatus> inferOrderTaskStatusOnSafeTestDataWithOrderSafeUnFree(){
    //其他维权进行中
    Map<Integer,TaskStatus> datas = Maps.newHashMap();
    datas.put(SafeState.BUYER_START.code(),TaskStatus.SUSPENDED);
    datas.put(SafeState.CLOSED.code(),null);
    datas.put(SafeState.FINISHED.code(),null);
    datas.put(SafeState.BUYER_RESTART.code(),null);
    datas.put(SafeState.BUYER_SENT.code(),null);
    datas.put(SafeState.INVOLVED.code(),null);
    datas.put(SafeState.SELLER_ACCEPTED.code(),null);
    datas.put(SafeState.SELLER_NOT_RECEIVED.code(),null);
    datas.put(SafeState.SELLER_REJECTED.code(),null);
    return datas;

  }
}
