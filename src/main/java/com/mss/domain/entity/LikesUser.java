package com.mss.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 点赞关联表(LikesUser)表实体类
 *
 * @author makejava
 * @since 2023-03-10 12:54:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("likes_user")
@Accessors(chain=true)
public class LikesUser  {

    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //点赞类型，0代表动态点赞，1代表评论点赞
    private Integer likeType;
    //被点赞的动态或者评论的id
    private Long belikedId;
    //用户id
    private Long userId;
    //创建时间
    private Date beLikeTime;



}

