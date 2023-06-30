package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDynamicsDto {


    //动态标题
    private String title;
    //动态内容
    @ApiModelProperty(value = "发布动态的文字内容")
    private String content;
//    //图片,最多9张，多张以<image>作为分隔符隔开
//    private String images;

}
