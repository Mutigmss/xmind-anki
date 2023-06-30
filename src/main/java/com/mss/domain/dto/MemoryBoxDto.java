package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryBoxDto {

    //闪记盒名称
    @ApiModelProperty(value = "卡片盒名字")
    private String boxName;
    //闪记盒封面
    @ApiModelProperty(value = "卡片盒封面照片")
    private String coverPhoto;

}
