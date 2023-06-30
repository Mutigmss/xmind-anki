package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.OptionCardDto;
import com.mss.domain.entity.OptionCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户笔记卡表(OptionCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:16:40
 */
public interface OptionCardService extends IService<OptionCard> {

    AjaxResult createOptionCard(OptionCardDto dto, HttpServletRequest request);

}


