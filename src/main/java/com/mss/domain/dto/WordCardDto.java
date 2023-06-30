package com.mss.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordCardDto {


    //单词
    private String word;
    //释义
    private String interpretation;
    //个人笔记
    private String personalNote;
    //所属卡片盒
    private String belongToBoxName;
    //所属卡片盒id
    private Long belongToBoxId;


}
