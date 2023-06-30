package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.FrontBackCardDto;
import com.mss.domain.entity.FrontBackCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 正反卡表(FrontBackCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-25 18:46:56
 */
public interface FrontBackCardService extends IService<FrontBackCard> {

    AjaxResult createFrontBackCard(FrontBackCardDto dto, HttpServletRequest request);
}


