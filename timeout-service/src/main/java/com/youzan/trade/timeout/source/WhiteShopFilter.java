package com.youzan.trade.timeout.source;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author apple created at: 16/1/21 下午8:51
 */
@Component("whiteShopFilter")
public class WhiteShopFilter {

  @Value("${env}")
  private String ENV;

  private String ENV_PRE = "pre";

  private List<Integer> whiteKdtIds = Lists.newArrayList(63077, 1422170, 2152574, 61256);

  public boolean filterKdtId(Integer kdtId) {
    if (ENV_PRE.equals(ENV)) {
      if (whiteKdtIds.contains(kdtId)) {
        return true;
      }

      return false;
    }

    return true;
  }

}
