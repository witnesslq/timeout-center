package com.youzan.trade.common.logging;

import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author Created by liwenjia@youzan.com on 2016/01/15 .
 */
public class ErrLogFilter extends AbstractMatcherFilter {

  @Override
  public FilterReply decide(Object event) {
    if (!isStarted())
    {
      return FilterReply.NEUTRAL;
    }

    LoggingEvent loggingEvent = (LoggingEvent) event;

    List<Level> eventsToKeep = Arrays.asList(Level.ERROR);
    if (eventsToKeep.contains(loggingEvent.getLevel()))
    {
      return FilterReply.NEUTRAL;
    }
    else
    {
      return FilterReply.DENY;
    }
  }
}
