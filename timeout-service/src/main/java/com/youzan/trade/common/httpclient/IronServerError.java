package com.youzan.trade.common.httpclient;

/**
 * @author Created by liwenjia@youzan.com on 2015/10/13 .
 */
public class IronServerError extends RuntimeException {

  public IronServerError(Exception e) {
    super(e);
  }
}
