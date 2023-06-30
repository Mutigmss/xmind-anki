package com.mss.domain.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 每日一句表(EverydaySentence)表实体类
 *
 * @author makejava
 * @since 2023-03-12 18:02:10
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("everyday_sentence")
public class EverydaySentence  {


    @TableId(value ="id",type = IdType.AUTO)
    private Long id;
    //内容
    private String content;
    //图片
    private String image;
    //译文
    private String translatedText;




}

