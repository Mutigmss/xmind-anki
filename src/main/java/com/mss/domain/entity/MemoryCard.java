package com.mss.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户记忆卡表(MemoryCard)表实体类
 *
 * @author makejava
 * @since 2023-03-18 14:47:40
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("memory_card")
public class MemoryCard {

    @ApiModelProperty(value = "卡片id")
    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    @ApiModelProperty(value = "卡片创建者id")
    private Long userId;

    //卡片类型
    @ApiModelProperty(value = "卡片类型")
    @TableField(exist = false)
    private String type;
    //问题
    @ApiModelProperty(value = "记忆卡问题")
    private String question;
    //答案
    @ApiModelProperty(value = "记忆卡答案")
    private String answer;

    //所属卡片盒名字
    @ApiModelProperty(value = "记忆卡所属卡片盒子名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "记忆卡所属卡片盒子id")
    private Long belongToBoxId;

    //上次学习的时间
    private Date lastLearnTime;
    //卡片累计学习次数
    private Integer learnCount;
    //是否停止学习，0为没有停止学习，1为已经停止学习
    private Integer isStopLearn;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;

}

