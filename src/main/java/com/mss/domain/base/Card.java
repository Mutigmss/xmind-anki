package com.mss.domain.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    //所属卡片盒
    @ApiModelProperty(value = "所属卡片盒名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;


}
