package com.youzan.trade.common.httpclient;

import com.youzan.trade.util.LogUtils;

import com.alibaba.dubbo.common.utils.StringUtils;

import org.apache.commons.collections.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpClientManager {

  public static final String CONTENT_TYPE_JSON = "application/json";
  protected CloseableHttpClient httpClient;

  /** 已封装的请求类型 */
  protected static final String GET = "get";
  protected static final String POST = "post";
  /** 请求编码,默认使用utf-8 */
  protected static final String CHARSET = "UTF-8";

  /** 连接超时时间,单位为毫秒,默认3000 */
  protected int connectionTimeout = 3000;
  /** 读取数据超时时间,单位为毫秒,默认3000 */
  protected int socketTimeout = 3000;

  protected int maxConnPerRoute = 250;

  protected int maxTotalConn = 500;

  public static final String API_HOST = "api.koudaitong.com";

  private final ResponseHandler<String> handler = new ResponseHandlerImpl();

  public void init() {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setDefaultMaxPerRoute(maxConnPerRoute);
    cm.setMaxTotal(maxTotalConn);

    if (httpClient == null) {
      httpClient = HttpClientBuilder.create()
          .setConnectionManager(cm)
          .setDefaultRequestConfig(buildRequestConfig(connectionTimeout, socketTimeout))
          .build();
    }
  }

  private RequestConfig buildRequestConfig(int connectionTimeout, int socketTimeout) {
    return RequestConfig.custom()
        .setConnectTimeout(connectionTimeout)
        .setSocketTimeout(socketTimeout)
        .build();
  }

  /**
   * HTTP Get 获取内容
   *
   * @param url    请求的url地址 ?之前的地址
   * @param params 请求的参数
   * @return 页面内容
   */
  public String doGet(String url, Map<String, String> params) {
    return doGet(url, params, CHARSET, socketTimeout);
  }

  /**
   * HTTP Post 获取内容
   *
   * @param url  请求的url地址 ?之前的地址
   * @param json 请求的参数,json格式
   * @return 页面内容
   */
  public String doPost(String url, String json) {
    return doPost(url, json, CHARSET, socketTimeout);
  }


  /**
   * HTTP Get 获取内容
   *
   * @param url     请求的url地址 ?之前的地址
   * @param params  请求的参数
   * @param charset 编码格式
   * @param timeout 超时时间ms
   * @return 页面内容
   */
  public String doGet(String url, Map<String, String> params, String charset, int timeout) {
    if (StringUtils.isBlank(url)) {
      return null;
    }
    try {
      url = appendUrlParams(url, params, charset);
      HttpGet httpGet = new HttpGet(url);
      // 设置请求和传输超时时间
      httpGet.setHeader("Host", API_HOST);
      httpGet.setConfig(buildRequestConfig(timeout, timeout));
      return httpClient.execute(httpGet, handler);
    } catch (UnsupportedEncodingException e) {
      LogUtils.error(log, "Convert params to url failed.params={},charset={},url={}", params,
                     charset,
                     url);
      throw new IronServerError(e);
    } catch (IOException e) {
      LogUtils.error(log, "ProcessHandlerFailed.url=" + url, e);
      throw new IronServerError(e);
    }
  }

  private String appendUrlParams(String url, Map<String, String> params, String charset)
      throws IOException {
    if (MapUtils.isNotEmpty(params)) {
      List<NameValuePair> pairs = new ArrayList<>(params.size());
      for (Map.Entry<String, String> entry : params.entrySet()) {
        if (entry.getValue() != null) {
          pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
      }
      //先判断一下传入的url中是否包含了"?"
      if (url.contains("?")) {
        url += "&";
      } else {
        url += "?";
      }
      url += EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
    }
    LogUtils.debug(log, "HttpClientManager.appendUrlParams=" + url);
    return url;
  }

  /**
   * HTTP Post 获取内容(只能用于请求java服务的接口)
   *
   * @param url     请求的url地址 ?之前的地址
   * @param json    请求的参数，json格式的字符串
   * @param charset 编码格式
   * @param timeout 超时时间ms
   * @return 页面内容
   */
  public String doPost(String url, String json, String charset, int timeout) {
    if (StringUtils.isBlank(url)) {
      return null;
    }
    try {
      HttpPost httpPost = new HttpPost(url);
      StringEntity entity = new StringEntity(json, charset);
      entity.setContentEncoding(charset);
      entity.setContentType(CONTENT_TYPE_JSON);
      httpPost.setEntity(entity);
      httpPost.setHeader("Host", API_HOST);

      // 设置请求和传输超时时间
      httpPost.setConfig(buildRequestConfig(timeout, timeout));
      return httpClient.execute(httpPost, handler);
    } catch (UnsupportedEncodingException e) {
      LogUtils.error(log, "Convert params to url failed.params={},json={},url={}",
                     json, charset, url);
      throw new IronServerError(e);
    } catch (ClientProtocolException e) {
      LogUtils.error(log, "params={},json={},url={}",
                     json, charset, url, e);
      throw new IronServerError(e);
    } catch (IOException e) {
      LogUtils.error(log, "params={},json={},url={}",
                     json, charset, url, e);
      throw new IronServerError(e);
    }
  }

  public void setMaxConnPerRoute(int maxConnPerRoute) {
    if (maxConnPerRoute < this.maxConnPerRoute) {
      LogUtils.error(log, "Invalid maxConnPerRoute={}", maxConnPerRoute);
      return;
    }
    LogUtils.debug(log, "MaxConnPerRoute Reload.MaxConnPerRoute={},Ori={}",
                   maxConnPerRoute,
                   this.maxConnPerRoute);
    this.maxConnPerRoute = maxConnPerRoute;
  }

  public void setMaxTotalConn(int maxTotalConn) {
    if (maxTotalConn < this.maxTotalConn) {
      LogUtils.error(log, "Invalid maxTotalConn={}", maxTotalConn);
      return;
    }
    LogUtils.debug(log, "MaxTotalConn Reload.MaxTotalConn={},Ori={}",
                   maxTotalConn,
                   this.maxTotalConn);
    this.maxTotalConn = maxTotalConn;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    if (connectionTimeout <= 0) {
      LogUtils.error(log, "Invalid connectionTimeoutMS={}", connectionTimeout);
      return;
    }
    LogUtils.debug(log, "ConnTO Reload.ConnTO={},Ori={}", connectionTimeout, this.connectionTimeout);
    this.connectionTimeout = connectionTimeout;
  }

  public void setSocketTimeout(int socketTimeout) {
    if (socketTimeout <= 0) {
      LogUtils.error(log, "Invalid soTimeout={}", socketTimeout);
      return;
    }
    LogUtils.debug(log, "SOTO Reload.ConnTO={},Ori={}", socketTimeout, this.socketTimeout);
  }

}
