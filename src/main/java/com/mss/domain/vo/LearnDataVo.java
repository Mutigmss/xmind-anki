package com.mss.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearnDataVo {

//    学习卡片次数
    private Integer  cardLearnCount;
//    连续打卡天数
//    private Integer  continuousClockInNumber;
//    累计学习卡牌数量
    private Integer learnCardNumber;
//    掌握的卡牌数量
    private Integer masterCardNumber;
//    用户卡片数量
    private Integer  cardNumber;


}
