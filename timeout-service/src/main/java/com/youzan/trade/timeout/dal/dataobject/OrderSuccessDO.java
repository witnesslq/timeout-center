package com.youzan.trade.timeout.dal.dataobject;

import lombok.Data;

/**
 * @author Created by liwenjia@youzan.com on 2015/12/29 .
 */
@Data
public class OrderSuccessDO {

  Integer id;
  String orderNo;
  Integer kdtId;
  Integer createTime;
  /**
   * 记录期间最后一条维权记录结束时间
   **/
  Integer finishTime;
  /**
   * 剩余时间
   **/
  Integer remainTime;
}
