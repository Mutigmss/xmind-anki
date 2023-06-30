package com.mss.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryBoxVo {

    @ApiModelProperty(value = "卡片盒子id")
    private Long id;

    @ApiModelProperty(value = "卡片盒子名字")
    private String boxName;

}
