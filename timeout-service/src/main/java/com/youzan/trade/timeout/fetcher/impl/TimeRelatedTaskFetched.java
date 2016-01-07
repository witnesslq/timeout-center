package com.youzan.trade.timeout.fetcher.impl;

import com.youzan.trade.timeout.fetcher.AbstractTaskFetcher;
import com.youzan.trade.timeout.fetcher.TaskFetcher;
import com.youzan.trade.timeout.model.DelayTask;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author apple created at: 16/1/5 下午7:44
 */
public class TimeRelatedTaskFetched extends AbstractTaskFetcher {

  /**
   * 起始时间,从9点到21点
   */
  private LocalTime startTime = LocalTime.of(9, 0);
  private LocalTime endTime = LocalTime.of(21, 0);

  private int incrementTimes = 2;

  @Override
  public List<DelayTask> fetch() {
    if (!validateTime(LocalDateTime.now())) {
      return Collections.emptyList();
    }

    return delayTaskService.getListWithBizTypeAndMsgTimeout(bizType.code(), mapDate(LocalDateTime.now()), maxSize);
  }

  private boolean validateTime(LocalDateTime someDateTime) {
    return !(someDateTime.toLocalTime().isBefore(startTime) || someDateTime.toLocalTime()
        .isAfter(endTime));
  }

  /**
   * 根据某个合法时间获取扫描时间点
   *
   * @param someDateTime 某个合法时间 时间部分在startTime和endTime之间
   * @return 扫描时间点
   */
  private Date mapDate(LocalDateTime someDateTime) {
    int incrementInSeconds = someDateTime.toLocalTime().toSecondOfDay() - startTime.toSecondOfDay();

    LocalDateTime startDateTime = LocalDateTime.of(someDateTime.toLocalDate(), startTime);
    LocalDateTime resultDateTime = startDateTime.plusSeconds(incrementInSeconds * incrementTimes);

    return Date.from(resultDateTime.toInstant(ZoneOffset.ofHours(8)));
  }

}
