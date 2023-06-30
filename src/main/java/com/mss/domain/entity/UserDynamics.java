package com.mss.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 用户动态表(UserDynamics)表实体类
 *
 * @author makejava
 * @since 2023-03-10 12:54:52
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_dynamics")
@Accessors(chain=true)
public class UserDynamics  {

//  id按照数据库里设置的只增规则自己增加
    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //动态id
    private Long dynamicsId;
    //动态标题
    private String title;
    //动态内容
    private String content;
    //图片,最多9张，多张以<image>作为分隔符隔开
    private String images;
    //创建者
    private Long createBy;
    //创建者名字
    private String createrName;
    //创建者头像
    private String createrAvatar;
    //点赞数
    private Long likeNumber;
    //是否被点赞
    @TableField(exist = false)
    private Boolean isLike;
    //评论数
    private Long commentNumber;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}

