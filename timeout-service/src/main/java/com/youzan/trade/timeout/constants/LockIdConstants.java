package com.youzan.trade.timeout.constants;

/**
 * @author apple created at: 15/11/5 下午6:10
 */
public class LockIdConstants {

  public static final int SAFE_EXECUTOR_LOCK_ID = 1;

  public static final int SAFE_MSG_EXECUTOR_LOCK_ID = 2;

  /**
   * 订单未付款
   */
  public static final int ORDER_UNPAID_EXECUTOR_LOCK_ID = 11;

  public static final int ORDER_UNPAID_MSG_EXECUTOR_LOCK_ID = 12;

  /**
   * 订单已发货
   */
  public static final int ORDER_DELIVERED_EXECUTOR_LOCK_ID = 21;

  public static final int ORDER_DELIVERED_MSG_EXECUTOR_LOCK_ID = 22;
}
