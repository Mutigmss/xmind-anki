package com.mss.exception;

import com.mss.enums.AppHttpCodeEnum;

public class CaptchaException  extends RuntimeException {

    String msg;
    int code;

    public int getCode() {
        return code;
    }

    public CaptchaException() {

    }

    public CaptchaException(String msg, int code) {
        this.msg = msg;
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CaptchaException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CaptchaException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
