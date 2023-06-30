package com.mss.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FillInBackCardDto {


    //填空卡内容
    private String content;
    //所属卡片盒名字
    @ApiModelProperty(value = "记忆卡所属卡片盒子名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "记忆卡所属卡片盒子id")
    private Long belongToBoxId;

}
