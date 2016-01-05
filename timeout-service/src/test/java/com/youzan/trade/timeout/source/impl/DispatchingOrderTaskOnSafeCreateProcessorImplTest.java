package com.youzan.trade.timeout.source.impl;

import junit.framework.TestCase;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/05 .
 */
public class DispatchingOrderTaskOnSafeCreateProcessorImplTest extends TestCase {


  public void jsonParseTest(){
    String msg = "{\"customer_type\":0,\"order_no\":\"E20160105104250015682179\",\"pay_state\":1,\"buyer_id\":2005148,\"feedback_time\":0,\"stock_state\":1,\"feedback\":0,\"tourist\":\"jbnp604oabjdspgttor0bqida4\",\"kdt_id\":3148,\"update_time\":1451961801,\"book_time\":1451961770,\"currency\":1,\"state\":6,\"order_type\":0,\"normal\":0,\"buyer_phone\":\"15557108560\",\"buy_way\":12,\"expire_time\":1451965370,\"express_time\":1451961801,\"close_time\":0,\"success_time\":0,\"pay_time\":1451961781,\"close_state\":0,\"express_state\":2,\"refund_time\":0,\"forbidUmp\":false,\"customer_name\":\"\",\"has_child\":0,\"customer_id\":0,\"refund_state\":0,\"express_type\":0}";
  }
}
