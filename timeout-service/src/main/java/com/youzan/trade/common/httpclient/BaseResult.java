package com.youzan.trade.common.httpclient;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Hub on 15/9/24.
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class BaseResult<T> {

    private T data;
    private String msg;
    /**正常响应前的异常信息*/
    private String preException;
    /**正常响应后的异常信息*/
    private int code;

    @JsonCreator
    public BaseResult(@JsonProperty("data")T data) {
        this.data = data;
    }
}
