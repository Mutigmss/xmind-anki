package com.mss.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNoteCardDto  {


    @ApiModelProperty(value = "卡片id")
    private Long id;
    //标题
    @ApiModelProperty(value = "标题")
    private String title;
    //笔记
    @ApiModelProperty(value = "笔记")
    private String note;
    //所属卡片盒
    @ApiModelProperty(value = "所属卡片盒名字")
    private String belongToBoxName;
    //所属卡片盒id
    @ApiModelProperty(value = "所属卡片盒子id")
    private Long belongToBoxId;



}
