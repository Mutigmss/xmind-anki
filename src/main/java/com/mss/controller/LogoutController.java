package com.mss.controller;

import com.mss.constant.TokenConstant;
import com.mss.domain.Result;
import com.mss.domain.entity.SysUser;
import com.mss.service.SysUserService;
import com.mss.utils.AjaxResult;
import com.mss.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * 退出管理
 */
@Api(tags = "退出登录")
@RestController
public class LogoutController {

    @Autowired
    private SysUserService service;

    @Autowired
    private RedisCache redisCache;

    @ApiOperation("用户注销登录，需要传token给后端")
    @PostMapping("/logout")
    private AjaxResult logout(HttpServletRequest request){

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        boolean issuccess = redisCache.deleteObject(TokenConstant.TOKEN_KEY + token);

        if(!issuccess){
            return AjaxResult.error();
        }
        return AjaxResult.success();

    }

}
