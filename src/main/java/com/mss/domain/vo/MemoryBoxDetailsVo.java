package com.mss.domain.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mss.domain.entity.MemoryBox;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemoryBoxDetailsVo {


    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    @ApiModelProperty(value = "卡片盒创建者id")
    private Long userId;
    //闪记盒封面
    @ApiModelProperty(value = "卡片盒封面")
    private String coverPhoto;
    //闪记盒名称
    @ApiModelProperty(value = "卡片盒名字")
    private String boxName;
    //父盒子id
    @ApiModelProperty(value = "卡片盒根id")
    private Long rootId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("总卡片数")
    private Integer totalCardNumber;

    @ApiModelProperty("停学卡片数")
    private Integer stopCardNumber;

    @ApiModelProperty("未学卡片数")
    private Integer unStudyCardNumber;

    @ApiModelProperty("记忆中卡片数")
    private Integer rememberCardNumber;

    @ApiModelProperty("以掌握卡片数")
    private Integer alreadyMasteredCardNumber;

    // 下级列表
    @ApiModelProperty(value = "卡片盒中的文件夹数组")
    @TableField(exist = false)
    private List<MemoryBox> childrenBox;


    // 顶级父盒子下的所有卡片数组
    @ApiModelProperty(value = "根盒子的全部卡片数组")
    @TableField(exist = false)
    private List<Object> cardList;

    // 顶级父盒子下的所有卡片数组
//    @ApiModelProperty(value = "根盒子的全部卡片数组")
//    @TableField(exist = false)
//    private List<Object> noStudyCardList;
//
//    // 顶级父盒子下的所有卡片数组
//    @ApiModelProperty(value = "根盒子的全部卡片数组")
//    @TableField(exist = false)
//    private List<Object> memoryCardList;
//
//
//    // 顶级父盒子下的所有卡片数组
//    @ApiModelProperty(value = "根盒子的全部卡片数组")
//    @TableField(exist = false)
//    private List<Object> stopLearnCardList;

}
