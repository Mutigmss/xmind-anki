package com.mss.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionCardVo {

    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //问题
    private String question;
    //选项
    private List<String> optionAarry;
    //答案
    private String answer;
    //解析
    private String analysis;
    //卡片类型
    @ApiModelProperty(value = "卡片类型")
    @TableField(exist = false)
    private String type;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;

}
