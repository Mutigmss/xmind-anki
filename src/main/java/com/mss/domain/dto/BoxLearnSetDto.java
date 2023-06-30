package com.mss.domain.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoxLearnSetDto {



    @ApiModelProperty(value = "学习场景")
    private String learnCondition;

    @ApiModelProperty(value = "学习目标")
    private String learnTarget;

    @ApiModelProperty(value = "每日学时,分钟")
    private String learnTime;

    @ApiModelProperty(value = "每日新卡上限")
    private String everdayNewCardLimit;

    @ApiModelProperty(value = "每日复习上限,张书")
    private String learnCardMaxNumber;

    @ApiModelProperty(value = "记忆强度,强，高，中，低")
    private String memoryStrength;

    @ApiModelProperty(value = "学习顺序,0新卡优先，1复习卡优先，2顺序优先")
    private Integer learnOrder;

    @ApiModelProperty(value = "新卡顺序，0添加顺序，1随机顺序")
    private Integer newCardOrder;


}
