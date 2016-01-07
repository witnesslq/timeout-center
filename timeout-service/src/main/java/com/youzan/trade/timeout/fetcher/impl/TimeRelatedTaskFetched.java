package com.youzan.trade.timeout.fetcher.impl;

import com.youzan.trade.timeout.fetcher.AbstractTaskFetcher;
import com.youzan.trade.timeout.fetcher.TaskFetcher;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * @author apple created at: 16/1/5 下午7:44
 */
@Component
public class TimeRelatedTaskFetched extends AbstractTaskFetcher {

  @Override
  public List<DelayTask> fetch() {
    return delayTaskService.getListWithBizTypeAndMsgTimeout(bizType.code(), mapDate(LocalDateTime.now()), maxSize);
  }

  private Date mapDate(LocalDateTime someLocalDateTime) {
    LocalTime resultLocalTime = mapLocalTime(someLocalDateTime.toLocalTime());

    LocalDateTime resultDateTime = LocalDateTime.of(someLocalDateTime.toLocalDate(), resultLocalTime);

    return Date.from(resultDateTime.toInstant(ZoneOffset.ofHours(8)));
  }

  private LocalTime mapLocalTime(LocalTime someLocalTime) {
    /**
     * 起始时间,从9点到21点
     */
    LocalTime startLocalTime = LocalTime.of(9, 0);
    LocalTime endLocalTime = LocalTime.of(21, 0);
    int incrementTimes = 2;

    if (someLocalTime.isBefore(startLocalTime)) {
      return LocalTime.MIN;
    } else if (someLocalTime.isAfter(endLocalTime)) {
      return LocalTime.MAX;
    } else {
      int incrementInSeconds = someLocalTime.toSecondOfDay() - startLocalTime.toSecondOfDay();
      return LocalTime.MIN.plusSeconds(incrementInSeconds * incrementTimes);
    }
  }

  public static void main(String[] args) {
    Date date1 = new TimeRelatedTaskFetched().mapDate(LocalDateTime.now());

    LocalTime startTime = LocalTime.of(9, 0);
    startTime.toSecondOfDay();


    LocalTime now = LocalTime.now();
    System.out.println("now: " + now);
    System.out.println("startTime: " + startTime);
    int i = now.compareTo(startTime);
    System.out.println(now.isAfter(startTime));

    //
    LocalTime min = LocalTime.MIN;
    min.plusSeconds(1000);

    //
    LocalDateTime localDateTime = LocalDateTime.now();

    new Date();
    Date date = Date.from(localDateTime.toInstant(ZoneOffset.ofHours(8)));
    System.out.println("当前时间: " + date);

  }
}
