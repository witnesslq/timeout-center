package com.youzan.trade.timeout.source;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * @author apple created at: 16/1/21 下午8:51
 */
public class WhiteShopFilter {

  @Value("${env}")
  public static String ENV;

  public static String ENV_PRE = "pre";

  public static List<Integer> whiteKdtIds = Lists.newArrayList(63077, 1422170, 2152574, 61256);

  public static boolean filterKdtId(Integer kdtId) {
    if (ENV_PRE.equals(ENV)) {
      if (whiteKdtIds.contains(kdtId)) {
        return true;
      }

      return false;
    }

    return true;
  }

}
