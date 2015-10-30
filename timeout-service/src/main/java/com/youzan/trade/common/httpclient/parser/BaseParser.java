package com.youzan.trade.common.httpclient.parser;

import com.youzan.trade.common.httpclient.BaseResult;
import com.youzan.trade.common.httpclient.IronResponseParser;
import com.youzan.trade.util.LogUtils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Created by liwenjia@youzan.com on 2015/10/16 .
 */
@Slf4j
public class BaseParser {

  static final ObjectMapper mapper = new ObjectMapper();

  static {
    try {
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      mapper.setDateFormat(fmt);
    }catch (Throwable t){
      LogUtils.error(log, "StaticBlock error", t);
    }
  }

  public static <T> BaseResult<T> parse(String responseStr, T type) {
    LogUtils.debug(log, "Client调用php结果:{}", responseStr);

    BaseResult<T> result = new BaseResult<>();

    BaseResult<String> parsedResponse = IronResponseParser.parse(responseStr);
    String responseJson = parsedResponse.getData();
    if (StringUtils.isNotBlank(responseJson)) {
      try {
        if (type instanceof String) {
          JsonNode node = mapper.readTree(responseJson);
          result = transJsonNode2BaseResult(node);
        } else {
          result = mapper.readValue(responseJson,
                                    mapper.getTypeFactory()
                                        .constructParametricType(BaseResult.class,
                                                                 type.getClass()));
        }
      } catch (Exception e) {
        LogUtils.warn(log, "Client调用php结果解析异常", e);
        //如果出现解析异常，可以尝试按list来解析结果
        List listType = Lists.newArrayList();
        try {
          BaseResult<List>
              blankResult =
              mapper.readValue(responseJson, mapper.getTypeFactory()
                  .constructParametricType(BaseResult.class, listType.getClass()));
          if (blankResult != null) {
            List data = blankResult.getData();
            result.setCode(blankResult.getCode());
            result.setMsg(blankResult.getMsg());
            result.setPreException(blankResult.getPreException());
            if(CollectionUtils.isEmpty(data)){
              return result;
            }else{
              LogUtils.error(log, "Mismatch type={},requestStr={}", type.getClass(), responseStr);
            }
          }
        } catch (IOException e1) {
          LogUtils.error(log, "Client调用php结果解析异常", e);
        }
      }
    }
    result.setPreException(parsedResponse.getPreException());
    return result;
  }

  private static <T> BaseResult<T> transJsonNode2BaseResult(JsonNode node) {
    if (node == null) {
      return null;
    }
    BaseResult<T> result = new BaseResult<>();
    if (node.has("code")) {
      result.setCode(node.get("code").getIntValue());
    }
    if (node.has("data")) {
      result.setData((T) (node.get("data").toString()));
    }
    if (node.has("msg")) {
      result.setMsg(node.get("msg").toString());
    }
    return result;
  }
}
