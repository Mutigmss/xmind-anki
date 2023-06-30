package com.mss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mutig
 * @version 1.0
 * @description app第三方微信登录接收参数实体类
 * @date 2023/6/21 0:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAppLoginDto {


    String  openId;
    String  nickName;
    String  avatarUrl;
//    String  unionId;
    String  accessToken;

}




