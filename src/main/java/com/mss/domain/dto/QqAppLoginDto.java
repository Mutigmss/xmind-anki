package com.mss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mutig
 * @version 1.0
 * @description QQ第三方登录接收参数
 * @date 2023/6/21 2:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QqAppLoginDto {

    String  openId;
    String  nickName;
    String  avatarUrl;
    String  gender;

}
