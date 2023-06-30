package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.BoxLearnSetDto;
import com.mss.domain.entity.PublicCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 公共模板卡表，所有卡都通用的公共卡(PublicCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-23 12:16:55
 */
public interface PublicCardService extends IService<PublicCard> {

    AjaxResult getAllCardByUserId(HttpServletRequest request);

    AjaxResult deleteCardById(Long id);

    AjaxResult getLearnCardListBySetting(BoxLearnSetDto dto, Long id, HttpServletRequest request);

    AjaxResult searchCardListByKeyWord(String keyword, HttpServletRequest request);

    AjaxResult searchCardListByKeyWordByMysql(String keyword, HttpServletRequest request);

    AjaxResult toLearn(HttpServletRequest request);

    AjaxResult learnCard(Long id,String learnStatus);

    PublicCard getByCardById(Long id);

    AjaxResult previewCardByUserId(HttpServletRequest request);

    AjaxResult getAllCardInBox(Long boxId,HttpServletRequest request);

    AjaxResult stopLearnCardById(Long id);

    AjaxResult resetCardById(Long id);
}


