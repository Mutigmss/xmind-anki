package com.mss.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.OosConstant;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.UserInfoVo;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.mapper.SysUserMapper;
import com.mss.service.OssService;
import com.mss.service.SysUserService;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import com.mss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.mss.service.impl.OssServiceImpl.getcontentType;

/**
 * 用户表信息(SysUser)表服务实现类
 *
 * @author makejava
 * @since 2023-02-25 14:46:04
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private OssService ossService;

    /**
     * 修改用户信息
     * @param userInfoVo
     * @param file
     * @param request
     * @return
     */
    @Override
    public AjaxResult updateUserInfo(UserInfoVo userInfoVo, MultipartFile file,
                                     HttpServletRequest request) {

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");

        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//        System.out.println(user.getId());
//        SysUser user1 = sysUserService.getById(user.getId());
        SysUser sysUser = BeanCopyUtils.copyBean(userInfoVo, SysUser.class);
        sysUser.setPassword(user.getPassword());
        sysUser.setCreateTime(user.getCreateTime());
        sysUser.setId(user.getId());

        boolean issuccess = update(sysUser,new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getId,sysUser.getId()));

        boolean b = redisCache.deleteObject(TokenConstant.TOKEN_KEY + token);

        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,sysUser,
                redisCache.getmaxOnineTime(), TimeUnit.SECONDS);

        return AjaxResult.success(userInfoVo);

    }


    /**
     * 更新用户头像
     * @param file
     * @return
     */
    @Override
    public AjaxResult updateUserAvatar(MultipartRequest file,Integer number,
                                       HttpServletRequest request) {


        //        创建图片二进制接收器数组
        List<MultipartFile> files = new ArrayList<>();

        System.out.println(number);
        System.out.println(file);


        String images=null;

//        把接收到的二进制图片添加进入接收器数组
        if(number!=null)
            if (number >0) {

                if(Objects.isNull(file)){
                    return AjaxResult.error("File request is null");
                }

                for (int i = 0; i < number; i++) {

                    MultipartFile file1 = file.getFile("file" + i);

                    if(!Objects.isNull(file1)){
                        files.add(file1);
                    }else{
                        throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                    }

                }

                //上传图片，返回图片地址
                images = ossService.updateImages(files);

            }

        final StringBuilder builder = new StringBuilder(images);
        int index = builder.indexOf("<image>");
        String s = builder.substring(0, index);

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


        user.setAvatar(s);
        boolean b = redisCache.deleteObject(TokenConstant.TOKEN_KEY + token);
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(), TimeUnit.SECONDS);

        boolean issuccess = sysUserService
                .update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));

        if(!issuccess){
            return AjaxResult.error();
        }

//        返回更新好的用户头像url地址
        return AjaxResult.success().put("avatar",s);


    }

    /**
     * 更新用户名
     * @param username
     * @param request
     * @return
     */
    @Override
    public AjaxResult updateusername(String username, HttpServletRequest request) {

        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        boolean issuccess = update().setSql("username=" + username)
                            .eq("id", user.getId()).update();

        user.setUsername(username);
        boolean b = redisCache.deleteObject(TokenConstant.TOKEN_KEY + token);
        redisCache.setCacheObject(TokenConstant.TOKEN_KEY +token,user,
                redisCache.getmaxOnineTime(), TimeUnit.SECONDS);

        boolean success = sysUserService
                .update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getId, user.getId()));

        if(!success){
            return AjaxResult.error();
        }

        return AjaxResult.success();

    }




}

