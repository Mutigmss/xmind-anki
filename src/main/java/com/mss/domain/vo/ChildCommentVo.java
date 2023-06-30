package com.mss.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class ChildCommentVo {


//    @ApiModelProperty(value = "ZI")

    private Long id;
    //动态内容
    private String content;
    //创建者
    private Long createBy;
    //创建者名字
    private String createrName;
    //创建者头像
    private String createrAvatar;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private Long toCommentId;
    //所回复的目标动态的作者昵称
    private String toCommentUserNickName;

    //是否被点赞
    @TableField(exist = false)
    private Boolean isLike;
    //是否为楼主
    @TableField(exist = false)
    private boolean isMaster;
    // 是否可以删除
    private boolean whetherCanDelete;

    //点赞数
    private Long likeNumber;
    //评论数
    private Long commentNumber;
    //创建时间
    private Date createTime;


}
