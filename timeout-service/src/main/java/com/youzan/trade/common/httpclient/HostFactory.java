package com.youzan.trade.common.httpclient;


import lombok.extern.slf4j.Slf4j;

/**
 * Created by Hub on 15/9/18.
 */
@Slf4j
public class HostFactory {
  /**
   * 请求iron-api层的接口的前缀host
   **/
  private static String API_HOST = "http://api.koudaitong.com/";

  /**
   * 根据方法的path和类型获取host字符串
   *
   * @param path 方法的path
   */
  public String getHost(String path) {

    String url;
    //将path中的"."替换局成"/"
    path = path.replace(".", "/");

    url = API_HOST + path + "?debug=test&format=json";
    return url;
  }

  public void setApiHost(String apiHost) {
    HostFactory.API_HOST = apiHost;
  }


}
