package com.mss.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionCardDto {


    //问题
    @ApiModelProperty("问题,可以以富文本的形式展示")
    private String question;
    //选项
    @ApiModelProperty("选项，不可以放图片和音频视频")
    private String option;
    //答案
    @ApiModelProperty("答案，不可以以富文本的形式展示")
    private String answer;
    //解析
    @ApiModelProperty("选项，可以以富文本的形式展示，即可以展示图片")
    private String analysis;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;


}
