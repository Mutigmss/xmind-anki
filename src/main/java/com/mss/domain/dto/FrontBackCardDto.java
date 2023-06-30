package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrontBackCardDto {

    //正面
    private String front;
    //反面
    private String back;
    //所属卡片盒名字
    @ApiModelProperty(value = "记忆卡所属卡片盒子名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "记忆卡所属卡片盒子id")
    private Long belongToBoxId;

}
