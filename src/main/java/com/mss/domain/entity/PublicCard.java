package com.mss.domain.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 公共模板卡表，所有卡都通用的公共卡(PublicCard)表实体类
 *
 * @author makejava
 * @since 2023-04-05 23:11:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("public_card")
public class PublicCard  {


    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //卡片类型
    private String type;
    //问题
    private String question;
    //答案
    private String answer;
    //标题
    private String title;
    //笔记
    private String note;
    //选项
    private String option;
    //解析
    private String analysis;
    //单词
    private String word;
    //释义
    private String interpretation;
    //正面
    private String front;
    //反面
    private String back;
    //内容
    private String content;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //是否停止学习，0为没有停止学习，1为已经停止学习
    private Integer isStopLearn;
    //个人笔记
    private String personalNote;
    //上次学习时间
    private Date lastLearnTime;
    //学习次数(0为未学习，1~5为记忆中，6为已经掌握）
    private Integer learnCount;

    public PublicCard(Long userId, String belongToBoxName, Long belongToBoxId) {
        this.userId = userId;
        this.belongToBoxName = belongToBoxName;
        this.belongToBoxId = belongToBoxId;
    }
}

