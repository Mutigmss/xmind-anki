package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChildMemoryBoxDto {

    @ApiModelProperty(value = "父卡片盒id")
    private Long fatherBoxId;

    @ApiModelProperty(value = "文件夹名字，即子卡片盒名字")
    private String boxName;

}
