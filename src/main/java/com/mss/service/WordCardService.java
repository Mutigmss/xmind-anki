package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.WordCardDto;
import com.mss.domain.entity.WordCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 智能单词卡表(WordCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:17:08
 */
public interface WordCardService extends IService<WordCard> {

    AjaxResult createWordCard(WordCardDto dto, HttpServletRequest request);

    AjaxResult translateWord(String text);
}


