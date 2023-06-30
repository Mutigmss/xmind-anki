package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ApiModelProperty(value = "卡片id")
//private Long id;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOptionCardDto {




    @ApiModelProperty(value = "卡片id")
    private Long id;
    //问题
    @ApiModelProperty("问题,可以以富文本的形式展示")
    private String question;
    //选项
    @ApiModelProperty("选项，不可以放图片和音频视频")
    private String option;
    //答案
    @ApiModelProperty("答案,可以以富文本的形式展示")
    private String answer;
    //解析
    @ApiModelProperty("选项，可以以富文本的形式展示，即可以展示图片")
    private String analysis;
    //所属卡片盒
//    @ApiModelProperty(value = "所属卡片盒名字")
//    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;

}
