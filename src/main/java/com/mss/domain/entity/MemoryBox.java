package com.mss.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * 用户闪记盒(MemoryBox)表实体类
 *
 * @author makejava
 * @since 2023-03-18 14:48:27
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("memory_box")
public class MemoryBox  {


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
    //是否停止学习，0为没有停止学习，1为已经停止学习
    private Integer isStopLearn;

    @ApiModelProperty(value = "学习场景,0为长期学习，1为期末复习,3未设置")
    private Integer learnCondition;
    @ApiModelProperty(value = "学习目标,0为简单了解，1为初步掌握，2为熟练运用,3未设置")
    private Integer learnTarget;
    @ApiModelProperty(value = "每日学时,分钟,默认30分钟")
    private Integer learnTime;
    @ApiModelProperty("考试时间")
    private Date examTime;
    @ApiModelProperty(value = "每日新卡上限")
    private Integer everydayNewCardLimit;
    @ApiModelProperty(value = "每日复习上限,张数")
    private Integer learnCardMaxNumber;
    @ApiModelProperty(value = "记忆强度,0强，1高，2中，3低")
    private Integer memoryStrength;
    @ApiModelProperty(value = "学习顺序,0新卡优先，1复习卡优先，2顺序优先")
    private Integer learnOrder;
    @ApiModelProperty(value = "新卡顺序，0添加顺序，1随机顺序")
    private Integer newCardOrder;



    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;


    @ApiModelProperty("停学卡片数")
    @TableField(exist = false)
    private Integer stopCardNumber;

    @ApiModelProperty("未学卡片数")
    @TableField(exist = false)
    private Integer noStudyCardNumber;

    @ApiModelProperty("记忆中卡片数")
    @TableField(exist = false)
    private Integer memoryCardNumber;

    @ApiModelProperty("以掌握卡片数")
    @TableField(exist = false)
    private Integer masterCardNumber;

    // 下级列表
    @ApiModelProperty(value = "卡片盒中的文件夹数组")
    @TableField(exist = false)
    private List<MemoryBox> childrenBox;

    // 盒子中的卡片
    @ApiModelProperty(value = "卡片盒中的卡片数组")
    @TableField(exist = false)
    private List<Object> cardList;


}

