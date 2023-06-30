package com.mss.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code; // 200是正常，非200表示异常
    private String msg;
    private Object data;


    public static Result ok(Object data) {
        return ok(200, "操作成功", data);
    }

    public static Result ok() {
        return ok(200, "操作成功", null);
    }

    public static Result ok(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }


    public static Result ok(int code, String msg){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }


    public static Result fail(String msg) {
        return fail(400, msg, null);
    }


    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }


    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}