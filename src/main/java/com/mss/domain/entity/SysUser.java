package com.mss.domain.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户表信息(SysUser)表实体类
 * @author makejava
 * @since 2023-02-25 15:00:26
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class SysUser  {


//  type = IdType.AUTO id按照数据库里设置的只增规则自己增加
    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户名
    private String username;
    //账号
    private String accountNumber;
    //密码
    private String password;
    //昵称
    private String nickName;
    //手机号
    private String phone;
    //角色
    private String role;
    //年龄
    private String age;
    //状态
    private String userStatus;
    //更新时间
    private Date updateTime;
    //创建时间
    private Date createTime;
    //是否删除(0-未删, 1-已删)
    private Integer isDeleted;
    //邮箱
    private String email;
    //头像图片
    private String avatar;
    
    private String gender;
    
    private String city;
    
    private String province;
    
    private String country;

    private String opendId;




}

