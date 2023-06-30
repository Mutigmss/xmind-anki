package com.mss.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemoryBoxDto {

    //闪记盒名称
    @ApiModelProperty(value = "卡片盒名字")
    private String boxName;
    //闪记盒封面
    @ApiModelProperty(value = "卡片id")
    private Long id;
    //用户id
    @ApiModelProperty(value = "卡片盒创建者id")
    private Long userId;
    //闪记盒封面
    @ApiModelProperty(value = "卡片盒封面")
    private String coverPhoto;

}
