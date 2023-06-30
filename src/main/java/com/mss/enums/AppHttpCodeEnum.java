package com.mss.enums;

public enum AppHttpCodeEnum {


    // 成功
    SUCCESS(200,"操作成功"),
    // 登录
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"服务器出现错误"),
    USERNAME_ERROR(509,"用户名错误"),
    PHONENUMBER_ERROR(510,"手机号错误"),
    EMAIL_ERROR(511, "邮箱错误"),
    PASSWORD_ERROR(505,"密码错误"),
    CAPTCHA_ERROR(508,"验证码错误！"),
    CAPTCHA_TIME_OUT(512,"验证码已过期"),
    EMAIL_NOT_EXIST(513,"邮箱不存在！请前往注册"),
    PHONENUMBER_NOT_EXIST(514,"手机号不存在，请前往注册"),
    USERNAME_NOT_EXIST(515,"用户名不存在,请前往注册"),
    ACCOUNT_NOT_EXITS(519,"该账号不存在，请先前往注册"),
    ACCOUNT_ERROR(520,"账号填写错误"),

    //注册
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    NICKNAME_EXIST(513,"昵称已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    ACCOUNT_EXIST(521,"账号已存在，无需注册"),
    ACCOUNT_INCORRECT_FORMAT(523,"账号格式不正确，账号限制为长度为9~13的纯数字字符串"),
    //用户身份校验拦截
    TOKEN_TIME_OUT(516,"token已经过期，请重新登录"),
    TOKEN_ERROR(517,"token错误"),

    COMMENT_NOT_NULL(506, "评论内容不能为空！"),
    FILE_TYPE_ERROR(507,"上传的文件类型错误，请上传png或者jpg文件"),
    USERNAME_NOT_NULL(507,"用户名不能为空"),
    EMAIL_NOT_NULL(508,"邮箱不能为空"),
    PASSWORD_NOT_NULL(509,"密码不能为空"),
    NICKNAME_NOT_NULL(510,"昵称不能为空"),


    NULL_Pointer_Exception(555,"空指针异常");

    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
