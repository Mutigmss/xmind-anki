package com.mss.filter;

import com.alibaba.fastjson.JSON;
import com.mss.domain.Result;
import com.mss.constant.TokenConstant;
import com.mss.domain.entity.LoginUser;
import com.mss.utils.AjaxResult;
import com.mss.utils.JwtUtil;
import com.mss.utils.RedisCache;
import com.mss.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


//jwt token校验过滤器
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private RedisCache redisCache;


    @Override
    protected void doFilterInternal(HttpServletRequest Request, HttpServletResponse Response, FilterChain filterChain) throws ServletException, IOException {


//      解析请求头
        String token = Request.getHeader("X-Token");

        if(!StringUtils.hasText(token)){
            //说明该接口不需要登录  直接放行
            filterChain.doFilter(Request, Response);
            return;
        }

//      System.out.println("jwt拦截请求头token成功！"+token);

        //解析获取userid
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token非法
            //响应告诉前端需要重新登录
            AjaxResult result = AjaxResult.error(516,TokenConstant.TOKEN_ERR);
            WebUtils.renderString(Response, JSON.toJSONString(result));
            return;
        }

        //   获取加密前的信息
//        String userId = claims.getSubject();
//        System.out.println(userId);

        //从redis中获取用户信息
        LoginUser loginUser = new LoginUser();

        loginUser.setUser(redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token));

        //如果获取不到
        if(Objects.isNull(loginUser)){
            //说明token过期  提示重新登录
//          Result result = Result.fail(TokenConstant.TOKEN_TIME_OUT);
            AjaxResult result = AjaxResult.error(517, TokenConstant.TOKEN_TIME_OUT);
            WebUtils.renderString(Response, JSON.toJSONString(result));
            return;
        }

        //存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(Request, Response);

    }
}
