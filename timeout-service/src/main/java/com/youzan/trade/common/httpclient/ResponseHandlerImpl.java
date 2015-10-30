package com.youzan.trade.common.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by liwenjia@youzan.com on 2015/10/09 .
 */
@Slf4j
public class ResponseHandlerImpl implements ResponseHandler {

  @Override
  public String handleResponse(HttpResponse response) throws IOException {
    StatusLine statusLine = response.getStatusLine();
    if (statusLine.getStatusCode() != 200) {
      throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
    }
    HttpEntity entity = response.getEntity();
    String result = null;
    if (entity != null) {
      result = EntityUtils.toString(entity, "utf-8");
    }else{
      throw new ClientProtocolException("Response contains no content");
    }
    EntityUtils.consume(entity);
    return result;
  }


}
