package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateFrontBackCardDto {

    @ApiModelProperty(value = "卡片id")
    private Long id;

    //正面
    @ApiModelProperty(value = "正面")
    private String front;
    //反面
    @ApiModelProperty(value = "反面")
    private String back;
    //所属卡片盒
    @ApiModelProperty(value = "所属卡片盒名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;
}
