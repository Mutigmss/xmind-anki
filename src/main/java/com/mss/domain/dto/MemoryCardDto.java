package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryCardDto {


    //问题
    @ApiModelProperty(value = "记忆卡问题")
    private String question;
    //答案
    @ApiModelProperty(value = "记忆卡答案")
    private String answer;
    //所属卡片盒
    @ApiModelProperty(value = "所属卡片盒名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;


}
