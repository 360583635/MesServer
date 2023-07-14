package com.job.common.result;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
//创建函数返回public R<xxx> xxx(){}
@Data
public class Result<T>  extends HashMap<String, Object> implements Serializable{

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据


    public static <T> Result<T> success(T object,String msg) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        result.msg = msg;
        return result;
    }


    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }


    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public static <T> Result<T> success() {
        return restResult(null, 0, "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return restResult(data, 0, "操作成功");
    }


    /*public static <T> Result<T> failure() {
        return restResult(null, 1, "操作失败");
    }

    public static <T> Result<T> failure(String msg) {
        return restResult(null, 1, msg);
    }

    public static <T> Result<T> failure(T data) {
        return restResult(data, 1, "操作失败");
    }

    public static <T> Result<T> failure(T data, String msg) {
        return restResult(data, 1, msg);
    }*/
    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.put("code", code);
        apiResult.put("data", data);
        apiResult.put("msg", msg);
        return apiResult;
    }
}

