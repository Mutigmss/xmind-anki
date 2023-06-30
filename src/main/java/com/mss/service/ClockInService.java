package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.entity.ClockIn;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 学习打卡表(ClockIn)表服务接口
 *
 * @author makejava
 * @since 2023-03-26 22:52:25
 */
public interface ClockInService extends IService<ClockIn> {

    AjaxResult getClockInRecordByUserId(HttpServletRequest request);

    AjaxResult userClockIn(HttpServletRequest request);
}


