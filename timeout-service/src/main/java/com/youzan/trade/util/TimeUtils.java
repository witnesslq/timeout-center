package com.youzan.trade.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by liwenjia@youzan.com on 2015/09/23 .
 */
public class TimeUtils {


  public static int currentInSeconds() {
    long current = System.currentTimeMillis();
    return (int)(current/1000);
  }

  public static Date getDateBySeconds(int seconds) {
    return new Date(((long) seconds) * 1000);
  }

  public static Date currentDate() {
    return Calendar.getInstance().getTime();
  }

  public static Date plusMilliSecond(Date date, long milliSecondsToPlus) {
    long milliseconds = date.getTime() + milliSecondsToPlus ;
    return new Date(milliseconds);
  }
}
