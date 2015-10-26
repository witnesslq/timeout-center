package com.youzan.trade.util;

/**
 * @author Created by liwenjia@youzan.com on 2015/09/23 .
 */
public class TimeUtils {


  public static int currentInSeconds(){
    long current = System.currentTimeMillis();
    return (int)(current/1000);
  }

}
