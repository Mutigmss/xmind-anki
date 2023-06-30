package com.mss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mss.domain.entity.LoginUser;
import com.mss.domain.entity.SysUser;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.mapper.SysUserMapper;
import com.mss.service.SysUserService;
import com.mss.utils.AuthenticationContextHolder;
import com.mss.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    public SysUserService sysUserService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<>();

        SysUser user =null;

//        if(RegexUtils.checkEmail(username)){
//
//            qw.eq(SysUser::getEmail,username);
//            user = sysUserService.getOne(qw);
//
//            if(Objects.isNull(user))
//            {
//                throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_EXIST);
//            }
//            return new LoginUser(user);
//
//        }else if(RegexUtils.checkPhone(username)){
//
//            qw.eq(SysUser::getPhone,username);
//            user = sysUserService.getOne(qw);
//
//            if(Objects.isNull(user))
//            {
//                throw new SystemException(AppHttpCodeEnum.PHONENUMBER_NOT_EXIST);
//            }
//            return new LoginUser(user);
//        }

        qw.eq(SysUser::getUsername,username);
        user = sysUserService.getOne(qw);

//        SysUser sysUser = sysUserMapper.baselogin(username);

        if(Objects.isNull(user))
        {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_EXIST);
        }

//        校验密码是否正确
//          validate(user);
//        System.out.println("密码正确");

        return new LoginUser(user);

    }

    @Autowired
    private PasswordEncoder Encoder;

    //    校验密码是否正确
    public void validate(SysUser user){

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
//        获取密码
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        System.out.println(password);
        System.out.println(user.getPassword());

//        密码验证
        boolean result = Encoder.matches(password, user.getPassword());


        if(!result){
            throw new RuntimeException("密码错误!");
        }

    }

}
