package com.youzan.trade.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * 封装一个日志公用类.目的
 * 1. 为了之后兼容各种日志系统时候不用到处更改代码
 * 2. 统一在调用写日志方法之前判断是否enable. 减少不必要的性能开销.
 *    由于同时为了满足目的1，所以没用通过AOP实现此方式
 * 3. 提供一个默认日志类，但是由于同这个日志打出来的日志文件不能清楚地在日志中显示类名，
 *    所以不推荐使用, 但是有些场景没有单独的logger的。可以使用
 *
 * @author wangxiaolei
 * create date: 15/9/9
 */
public class LogUtils {

  private static Logger defaultLogger = LoggerFactory.getLogger(LogUtils.class);


  /*******************************************************/
  /*****************  debug level    *********************/
  /*******************************************************/

  public static void debug(Logger logger, String message) {
    if(logger.isDebugEnabled()) {
      logger.debug(message);
    }
  }
  public static void debug(Logger logger, String message, Throwable throwable) {
    if(logger.isDebugEnabled()) {
      logger.debug(message, throwable);
    }
  }
  public static void debug(Logger logger, String format, Object... arguments) {
    if(logger.isDebugEnabled()) {
      logger.debug(format, arguments);
    }
  }
  @Deprecated
  public static void debug(String message) {
    debug(defaultLogger, message);
  }
  @Deprecated
  public static void debug(String message, Throwable throwable) {
    debug(defaultLogger, message, throwable);
  }
  @Deprecated
  public static void debug(String message, Object... arguments) {
    debug(defaultLogger, message, arguments);
  }

  /*******************************************************/
  /*****************  info  level    *********************/
  /*******************************************************/

  public static void info(Logger logger, String message) {
    if(logger.isInfoEnabled()) {
      logger.info(message);
    }
  }
  public static void info(Logger logger, String message, Throwable throwable) {
    if(logger.isInfoEnabled()) {
      logger.info(message, throwable);
    }
  }
  public static void info(Logger logger, String format, Object... arguments) {
    if(logger.isInfoEnabled()) {
      logger.info(format, arguments);
    }
  }
  @Deprecated
  public static void info(String message) {
    info(defaultLogger, message);
  }
  @Deprecated
  public static void info(String message, Throwable throwable) {
    info(defaultLogger, message, throwable);
  }
  @Deprecated
  public static void info(String message, Object... arguments) {
    info(defaultLogger, message, arguments);
  }

  /*******************************************************/
  /*****************  warn level     *********************/
  /*******************************************************/

  public static void warn(Logger logger, String message) {
    if(logger.isWarnEnabled()) {
      logger.warn(message);
    }
  }
  public static void warn(Logger logger, String message, Throwable throwable) {
    if(logger.isWarnEnabled()) {
      logger.warn(message, throwable);
    }
  }
  public static void warn(Logger logger, String format, Object... arguments) {
    if(logger.isWarnEnabled()) {
      logger.warn(format, arguments);
    }
  }
  @Deprecated
  public static void warn(String message) {
    warn(defaultLogger, message);
  }
  @Deprecated
  public static void warn(String message, Throwable throwable) {
    warn(defaultLogger, message, throwable);
  }
  @Deprecated
  public static void warn(String message, Object... arguments) {
    warn(defaultLogger, message, arguments);
  }

  /*******************************************************/
  /*****************  error level    *********************/
  /*******************************************************/
  public static void error(Logger logger, String message) {
    if(logger.isErrorEnabled()) {
      logger.error(message);
    }
  }
  public static void error(Logger logger, String message, Throwable throwable) {
    if(logger.isErrorEnabled()) {
      logger.error(message, throwable);
    }
  }
  public static void error(Logger logger, String format, Object... arguments) {
    if(logger.isErrorEnabled()) {
      logger.error(format, arguments);
    }
  }
  @Deprecated
  public static void error(String message) {
    error(defaultLogger, message);
  }
  @Deprecated
  public static void error(String message, Throwable throwable) {
    error(defaultLogger, message, throwable);
  }
  @Deprecated
  public static void error(String message, Object... arguments) {
    error(defaultLogger, message, arguments);
  }

  /*******************************************************/
  /*****************  trace level    *********************/
  /*******************************************************/

  public static void trace(Logger logger, String message) {
    if(logger.isTraceEnabled()) {
      logger.trace(message);
    }
  }
  public static void trace(Logger logger, String message, Throwable throwable) {
    if(logger.isTraceEnabled()) {
      logger.trace(message, throwable);
    }
  }
  public static void trace(Logger logger, String format, Object... arguments) {
    if(logger.isTraceEnabled()) {
      logger.trace(format, arguments);
    }
  }
  @Deprecated
  public static void trace(String message) {
    trace(defaultLogger, message);
  }
  @Deprecated
  public static void trace(String message, Throwable throwable) {
    trace(defaultLogger, message, throwable);
  }
  @Deprecated
  public static void trace(String message, Object... arguments) {
    trace(defaultLogger, message, arguments);
  }

}
