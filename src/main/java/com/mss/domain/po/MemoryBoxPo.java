package com.mss.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.mss.domain.entity.MemoryBox;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryBoxPo {


    private Long id;
    //用户id
    @ApiModelProperty(value = "卡片盒创建者id")
    private Long userId;
    //闪记盒名称
    @ApiModelProperty(value = "卡片盒名字")
    private String boxName;
    //父盒子id
    @ApiModelProperty(value = "卡片盒根id")
    private Long rootId;

    // 下级列表
    @ApiModelProperty(value = "卡片盒中的文件夹数组")
    @TableField(exist = false)
    private List<MemoryBox> childrenBox;

}
