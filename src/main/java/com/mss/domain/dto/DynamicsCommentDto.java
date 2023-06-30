package com.mss.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicsCommentDto {



    //评论的根id
//    @ApiModelProperty(value = "动态评论的根id")
//    private Long rootId;
    //动态内容
    @ApiModelProperty(value = "动态内容")
    private String content;
    //所回复的目标动态的用户id
    @ApiModelProperty(value = "所回复的目标动态的用户id")
    private Long toDynamicsUserId;
    //回复目标动态id
    @ApiModelProperty(value = "回复目标动态id")
    private Long toDynamicsId;

//    创建者
//    @ApiModelProperty(value = "创建者id")
//    private Long createBy;



}
