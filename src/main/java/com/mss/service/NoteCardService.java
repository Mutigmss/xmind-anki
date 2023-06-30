package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.NoteCardDto;
import com.mss.domain.entity.NoteCard;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户笔记卡表(NoteCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-21 17:22:38
 */
public interface NoteCardService extends IService<NoteCard> {

    AjaxResult createNoteCard(NoteCardDto dto, HttpServletRequest request);
}


