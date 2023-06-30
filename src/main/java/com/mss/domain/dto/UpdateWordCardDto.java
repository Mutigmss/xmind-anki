package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateWordCardDto {



    @ApiModelProperty(value = "卡片id")
    private Long id;
    //单词
    @ApiModelProperty(value = "单词")
    private String word;
    //释义
    @ApiModelProperty(value = "释义")
    private String interpretation;
    //个人笔记
    @ApiModelProperty(value = "个人笔记")
    private String personalNote;
    //所属卡片盒
//    @ApiModelProperty(value = "所属卡片盒名字")
//    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;


}
