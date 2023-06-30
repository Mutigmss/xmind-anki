package com.mss.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVo {


    //主键
//    private Long id;
    //用户名
    private String username;
    //昵称
    private String nickName;
    //手机号
    private String phone;
    //头像图片
    private String avatar;
    //性别
    private String gender;
    //邮箱
    private String email;


}
