package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表信息(SysUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-25 14:46:04
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 基础登录(用户名，手机号码，邮箱登录)
     * @param username
     * @return
     */
    SysUser baselogin(String username);




}
