package com.mss.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo {

    //主键
    private Long id;
    //用户名
    private String username;
    //密码
    private String password;
    //昵称
    private String nickName;
    //性别
    private String gender;
    //手机号
    private String phone;
    //角色
    private String role;
    //年龄
    private String age;
    //状态
    private String userStatus;
    //是否删除(0-未删, 1-已删)
    private Integer isDeleted;
    //邮箱
    private String email;


}
