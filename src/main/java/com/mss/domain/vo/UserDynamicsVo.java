package com.mss.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDynamicsVo {




    private Long id;
    //动态id
    //private Long dynamicsId;
    //动态标题
    private String title;
    //动态内容
    private String content;
    //图片,最多9张，多张以<image>作为分隔符隔开
    private List<String> imagesArray;
    //创建者
    private Long createBy;
    //用户信息
    private SysUserVo2 userInfo;

    // 是否可以删除
    private boolean whetherCanDelete;

    //点赞数
    private Long likeNumber;
    @TableField(exist = false)
    private Boolean isLike;

    //评论数
    private Long commentNumber;

    //创建时间
    private Date createTime;



}
