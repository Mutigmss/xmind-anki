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
 * 智能单词卡表(WordCard)表实体类
 *
 * @author makejava
 * @since 2023-03-23 12:17:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("word_card")
public class WordCard  {


    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //卡片类型
    @ApiModelProperty(value = "卡片类型")
    @TableField(exist = false)
    private String type;
    //单词
    private String word;
    //释义
    private String interpretation;
    //个人笔记
    private String personalNote;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;
    //上次学习的时间
    private Date lastLearnTime;
    //卡片累计学习次数
    private Integer learnCount;
    //是否停止学习，0为没有停止学习，1为已经停止学习
    private Integer isStopLearn;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;




}

