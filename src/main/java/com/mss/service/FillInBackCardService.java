package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.FillInBackCardDto;
import com.mss.domain.entity.FillInBackCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 填空表(FillInBackCard)表服务接口
 *
 * @author makejava
 * @since 2023-04-20 21:46:31
 */
public interface FillInBackCardService extends IService<FillInBackCard> {

    AjaxResult createFillInBackCard(FillInBackCardDto dto, HttpServletRequest request);
}


