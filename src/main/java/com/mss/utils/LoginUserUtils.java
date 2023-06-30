package com.mss.utils;

import com.mss.constant.TokenConstant;
import com.mss.domain.entity.SysUser;

import javax.servlet.http.HttpServletRequest;


/**
 * 获取登录用户工具类，工具前端返回的token获取redis中存储的用户信息
 * @author mss
 *
 */
public class LoginUserUtils {

    public static SysUser getLoginUser(HttpServletRequest request,
                                       RedisCache redisCache){

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);
        return user;

    }

}
