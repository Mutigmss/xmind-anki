package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.UserInfoVo;
import com.mss.utils.AjaxResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户表信息(SysUser)表服务接口
 *
 * @author makejava
 * @since 2023-02-25 14:46:04
 */
public interface SysUserService extends IService<SysUser> {


    AjaxResult updateUserInfo(UserInfoVo userInfoVo, MultipartFile file,HttpServletRequest request);

    AjaxResult updateUserAvatar(MultipartRequest file,Integer number, HttpServletRequest request);

    AjaxResult updateusername(String username, HttpServletRequest request);


}


