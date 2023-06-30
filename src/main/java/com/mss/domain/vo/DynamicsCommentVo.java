package com.mss.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class DynamicsCommentVo {


    private Long id;
    //评论类型（0代表动态评论，1代表评论的子评论）
    private String type;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //图片,最多9张，多张以<image>作为分隔符隔开
    private String images;
    private List<String> imageArray;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //回复目标评论id
    private Long toCommentId;
    //创建者
    private Long createBy;
    //创建者名字
    private String createrName;
    //创建者头像
    private String createrAvatar;
    //点赞数
    private Long likeNumber;
    //评论数
    private Long commentNumber;
    //是否被点赞
    @TableField(exist = false)
    private Boolean isLike;

    private Date createTime;

    private Long updateBy;

    // 是否可以删除
    private boolean whetherCanDelete;

    private List<DynamicsCommentVo2> childComment;




}
