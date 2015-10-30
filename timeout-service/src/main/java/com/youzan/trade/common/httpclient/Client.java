package com.youzan.trade.common.httpclient;


import com.youzan.trade.common.httpclient.parser.BaseParser;
import com.youzan.trade.util.LogUtils;

import com.alibaba.fastjson.JSON;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

  @Setter
  private static HttpClientManager httpClientManager;
  @Setter
  private static HostFactory hostFactory;

  public void setHostFactory(HostFactory hostFactory) {
    Client.hostFactory = hostFactory;
  }

  public void setHttpClientManager(HttpClientManager httpClientManager) {
    Client.httpClientManager = httpClientManager;
  }

  /**
   * 调用iron(php)的api层的接口,默认为POST请求(目前看POST可以满足所有场景)
   *
   * @param path   接口在iron的api文件夹下的路径
   * @param object 接口的入参,对象入参
   * @return 返回map
   */
  public static <T> BaseResult<T> call(String path, Object object, T responseType) {
    LogUtils.debug(log, "order[] call[{}] param[{}]", path, JSON.toJSONString(object));
    String json = JSON.toJSONString(object);
    return Client.call(path, json, responseType);
  }

  /**
   * 调用http请求的接口(基础接口)
   *
   * @param path 接口的路径,不包括host
   * @param json 接口入参，json格式字符串
   */
  public static <T> BaseResult<T> call(String path, String json, T responseType) {
    String url = hostFactory.getHost(path);
    LogUtils.debug(log, "java->php[{}] call url: {}", path, url);
    long startTime = System.currentTimeMillis();
    String result = httpClientManager.doPost(url, json);
    LogUtils.debug(log, "java->php[{}] run time: {} ms", path,
                   System.currentTimeMillis() - startTime);
    return BaseParser.parse(result, responseType);
  }
}
