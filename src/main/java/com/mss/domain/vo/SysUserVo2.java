package com.mss.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserVo2 {


    private String username;
    //昵称
    private String nickName;
    //头像图片
    private String avatar;

}
