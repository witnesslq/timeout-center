package com.youzan.trade.timeout.source.impl;

import com.youzan.trade.timeout.constants.SafeState;
import com.youzan.trade.timeout.model.Safe;
import com.youzan.trade.timeout.service.SafeService;

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
  public void testInferOrderTaskStatusOnSafe() throws Exception {
    //TODO
    String orderNo = "E123";
    Integer kdtId= 1;
    Safe safe = new Safe();
    safe.setOrderNo(orderNo);
    safe.setKdtId(kdtId);
    when(safeService.checkOrderFeedbackFinish(orderNo, kdtId)).thenReturn(Boolean.FALSE);
    dispatchingOrderTaskOnSafeProcessor.inferOrderTaskStatusOnSafe(safe);
  }
}
