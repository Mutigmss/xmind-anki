package com.mss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mutig
 * @version 1.0
 * @description 第三方登录接收参数实体类
 * @date 2023/6/21 1:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyLoginDto {

    String  openId;
    String  nickName;
    String  avatarUrl;
    String  unionId;
    String  accessToken;
    String  loginType;

}
