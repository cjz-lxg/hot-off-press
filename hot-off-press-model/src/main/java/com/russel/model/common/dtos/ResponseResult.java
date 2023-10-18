package com.russel.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.russel.model.common.enums.AppHttpCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的结果返回类
 * @param <T>
 */
@Data
public class ResponseResult<T> implements Serializable {

    private String host;

    private Integer code;

    private String message;

    private T data;

    public ResponseResult() {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }


    public static ResponseResult<?> errorResult(int code, String msg) {
        ResponseResult<String> result = new ResponseResult<String>();
        return result.error(code, msg);
    }

    public static ResponseResult<?> okResult(int code, String msg) {
        ResponseResult<String> result = new ResponseResult<String>();
        return result.ok(code, null, msg);
    }

    public static ResponseResult<?> okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult<?> errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMessage());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String message){
        return setAppHttpCodeEnum(enums,message);
    }

    public static ResponseResult<?> setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getMessage());
    }

    private static ResponseResult<?> setAppHttpCodeEnum(AppHttpCodeEnum enums, String message){
        return okResult(enums.getCode(),message);
    }

    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
        return this;
    }

    public ResponseResult<T> ok(T data) {
        this.data = data;
        return this;
    }


    public static void main(String[] args) {
        //前置
        /*AppHttpCodeEnum success = AppHttpCodeEnum.SUCCESS;
        System.out.println(success.getCode());
        System.out.println(success.getmessage());*/

        //查询一个对象
        /*Map<String,Object> map = new HashMap<String,Object>();
        map.put("name","zhangsan");
        map.put("age",18);
        ResponseResult<?> result = ResponseResult.okResult(map);
        System.out.println(JSON.toJSONString(result));*/


        //新增，修改，删除  在项目中统一返回成功即可
        /*ResponseResult result = ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        System.out.println(JSON.toJSONString(result));*/


        //根据不用的业务返回不同的提示信息  比如：当前操作需要登录、参数错误
        /*ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN,"自定义提示信息");
        System.out.println(JSON.toJSONString(result));*/

        //查询分页信息
        PageResponseResult responseResult = new PageResponseResult(1,5,50);
        List list = new ArrayList();
        list.add("itcast");
        responseResult.setData(list);
        System.out.println(JSON.toJSONString(responseResult));
    }

}
