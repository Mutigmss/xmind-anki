package com.mss.exception;

import com.mss.enums.AppHttpCodeEnum;

public class PasswordException  extends RuntimeException{

    int code;
    String msg;

    public PasswordException(String msg) {
        this.msg = msg;
    }
    public PasswordException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
