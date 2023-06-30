package com.mss.exception;

import com.mss.enums.AppHttpCodeEnum;
import com.mss.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice//作用相当于ResponseBody和ControllerAdvice
@Slf4j//日志
public class GlobalExceptionHandler {

    @ExceptionHandler(SystemException.class)
    public AjaxResult systemExceptionHandler(SystemException e){
        //打印异常信息
        log.error("出现了异常！ {}",e);//{}占位符
        //从异常对象中获取提示信息封装返回
        return AjaxResult.error(e.getCode(),e.getMsg());
    }


    @ExceptionHandler(Exception.class)
    public AjaxResult exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return AjaxResult.error(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),e.getMessage());
    }

    @ExceptionHandler(PasswordException.class)
    public AjaxResult passwordHandler(PasswordException e){

        //打印异常信息
        log.error("出现了异常！ {}",e);
        return  AjaxResult.error(e.getCode(),e.getMsg());


    }

    @ExceptionHandler(CaptchaException.class)
    public AjaxResult captchaHandler(CaptchaException e){

        //打印异常信息
        log.error("出现了异常！ {}",e);
        //从异常对象中获取提示信息封装返回
        return AjaxResult.error(e.getCode(),e.getMsg());

    }



}