package com.youzan.trade.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Created by liwenjia@youzan.com on 2015/09/23 .
 */
public class TimeUtils {

  // 2016/1/25 00:00:00
  private static int startTime = 1453651200;

  // 2016/2/17 00:00:00
  private static int endTime = 1455638400;

  public static boolean isInSpring2016(int delayStartTime) {
    return delayStartTime >= startTime && delayStartTime < endTime;
  }

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
    long milliseconds = date.getTime() + milliSecondsToPlus;
    return new Date(milliseconds);
  }

  public static int getSecondFromDate(Date date){
    return (int)(date.getTime()/1000);
  }
}
