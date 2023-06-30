package com.mss.aspect;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mss.domain.entity.LikesUser;
import com.mss.service.LikesUserService;
import com.mss.service.UserDynamicsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//定义通知
//@Component
//@Aspect
public class DynamicsLikeAspect {


    @Autowired
    private LikesUserService likesUserService;

    @Autowired
    private UserDynamicsService DynamicsService;

    //切入点：待增强的方法,即用户点赞功能的增强方法
    @Pointcut("execution( * com.mss.service.UserDynamicsService.likeDynamics(..))")
    //切入点签名
    public void strengthen(){
        System.out.println("pointCut签名。。。");
    }

    //后置通知
    @After("strengthen()")
    public void after(JoinPoint jp){

//        List<LikesUser> list =
//                likesUserService.list(new LambdaQueryWrapper<LikesUser>()
//                                      .eq(LikesUser::getLikeType, 0));

        List<LikesUser> lists = likesUserService.list();
        List<LikesUser> list1 = lists.stream()
                .filter(list -> list.getLikeType().equals(0))
                .collect(Collectors.toList());

        System.out.println("后置通知：最后且一定执行.....");
    }

}
