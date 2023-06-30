package com.mss.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户学习卡片次数表（学习卡片的次数，相同卡片可以重复学习）(UserLearn)表实体类
 *
 * @author makejava
 * @since 2023-03-27 15:54:23
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_learn")
public class UserLearn  {
    
    private Long id;
    //用户id
    private Long userId;
    //卡片id
    private Long cardId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;




}

