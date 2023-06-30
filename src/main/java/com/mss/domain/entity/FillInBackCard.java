package com.mss.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 填空表(FillInBackCard)表实体类
 *
 * @author makejava
 * @since 2023-04-20 21:46:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("填空卡")
public class FillInBackCard  {

    @ApiModelProperty(value="id")
    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //内容
    private String content;
    //卡片类型
    @ApiModelProperty(value = "卡片类型")
    @TableField(exist = false)
    private String type;
    //上次学习的时间
    private Date lastLearnTime;
    //卡片累计学习次数
    private Integer learnCount;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;
    //是否停学
    private Integer isStopLearn;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;




}

