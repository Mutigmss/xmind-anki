package com.mss.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClockInVo {


    //当天日期
    private String todayTime;

    //该月天数
    private int totalDay;
    //打卡日期数组
    private boolean[] clockInArray;



}
