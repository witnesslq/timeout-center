package com.youzan.trade.common.httpclient;


/**
 * @author Created by liwenjia@youzan.com on 2015/10/15 .
 */
public class IronResponseParser {

  private static final String TAG_CODE = "code";
  private static final String TAG_MSG = "msg";
  private static final String TAG_DATA = "data";

  int preExpIdx,contentIdx, postExpIdx = -1;
  String tag = null;
  String pre = null;
  String detectMsg = null;

  public static BaseResult<String> parse(String content) {
    return new IronResponseParser().extractIronResp(content);
  }

  /**
   * 先处理异常+数据响应
   **/
  public BaseResult<String> extractIronResp(String content) {
    if (content == null) {
      return null;
    }
    BaseResult<String> result = new BaseResult();
    //`{`探测 首先判断返回结果是否包含json数据
    for (int i = 0; i < content.length(); i++) {
      char c = content.charAt(i);
      if (c >= 'A' && c <= 'Z') {
        c = (char) (c - ('A' - 'a'));
      }
      if (c == ' ' || c == '\n' || c=='\t') {
        continue;
      }
      if (pre == null) {
        if (c == '{') {
          pre = "{";
          contentIdx = i;
          continue;
        }
        if (preExpIdx < 0) {
          preExpIdx = i;
        }
      } else {
        if (tag == null) {
          if (pre.equals("{")) {
            if (c == '"') {
              pre = "{\"";
              continue;
            }
            reset();
          } else if (pre.equals("{\"")) {
            if (c == 'c') {
              tag = TAG_CODE;
              detectMsg = "c";
              continue;
            } else if (c == 'd') {
              tag = TAG_DATA;
              detectMsg = "d";
              continue;
            } else if (c == 'm') {
              tag = TAG_MSG;
              detectMsg = "m";
              continue;
            }
            reset();
          }
        } else {
          detectMsg += c;
          if (tag.startsWith(detectMsg)) {
            if (tag.equals(detectMsg)) {
              String preExcept = content.substring(preExpIdx,contentIdx);
              String contentStr = content.substring(contentIdx, content.length());
              result.setPreException(preExcept);
              result.setData(contentStr);
              return result;
            }
          } else {
            reset();
          }
        }
      }
    }

    result.setPreException(content);

    return result;
  }

  private void reset() {
    contentIdx = -1;
    tag = null;
    pre = null;
    detectMsg = null;
  }
}
