package com.mss.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Document(indexName = "testcard")
public class EsPublicCard {



    private Long id;
    //用户id
    private Long userId;
    //卡片类型
    private String type;
    //问题
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String question;
    //答案
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String answer;
    //标题
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String title;
    //笔记
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String note;
    //选项
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String option;
    //解析
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String analysis;
    //单词
    @Field(type = FieldType.Keyword)
    private String word;
    //释义
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String interpretation;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //个人笔记
    private String personalNote;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;
    //卡片内容是否为公共内容（0为公共内容，1为用户自己的内容）
    private Integer isPublicContent;
    //正面
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String front;
    //反面
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String back;

}
