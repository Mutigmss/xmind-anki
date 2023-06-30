package com.mss.domain.entity;

import java.util.Date;
import java.io.Serializable;

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


/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2023-03-10 12:53:05
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment  {

    // type = IdType.AUTO id按照数据库里设置的只增规则自己增加
    @ApiModelProperty(value="id")
    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //评论类型（0代表动态评论，1代表评论的子评论）
    @ApiModelProperty(value="评论类型（0代表动态评论，1代表评论的子评论）")
    private String type;
    //评论id
    @ApiModelProperty(value="评论id")
    private Long commentId;
    //根评论id
    @ApiModelProperty(value="根id")
    private Long rootId;
    //评论内容
    @ApiModelProperty("内容")
    private String content;
    //图片,最多9张，多张以<image>作为分隔符隔开
    @ApiModelProperty("图片url字符串")
    private String images;
    //所回复的目标评论的userid
    @ApiModelProperty("回复的目标评论的用户id")
    private Long toCommentUserId;
    //回复目标评论id
    @ApiModelProperty("回复的目标评论的id")
    private Long toCommentId;
    //创建者
    @ApiModelProperty("创建者id")
    private Long createBy;
    //创建者名字
    @ApiModelProperty("创建者名字")
    private String createrName;
    //创建者头像
    @ApiModelProperty("创建者头像url")
    private String createrAvatar;

    //点赞数，评论的子评论无点赞数
    @ApiModelProperty("点赞数")
    private Long likeNumber;

    //是否被点赞
    @TableField(exist = false)
    private Boolean isLike;

    //评论的评论数
    @ApiModelProperty("评论数")
    private Long commentNumber;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新者id")
    private Long updateBy;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;




}

