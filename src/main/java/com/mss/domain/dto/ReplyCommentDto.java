package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCommentDto {


    //评论的根id
//    private Long rootId;
    //动态内容
    @ApiModelProperty(value = "回复评论的内容")
    private String content;
    //所回复的目标动态的用户id
    @ApiModelProperty(value="回复目的评论的用户id")
    private Long toCommentUserId;
    //回复目标动态id
    @ApiModelProperty(value="回复目的评论的id")
    private Long toCommentId;
    //创建者
//    private Long createBy;

}
