package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.MemoryCardDto;
import com.mss.domain.entity.MemoryCard;
import com.mss.utils.AjaxResult;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;


/**
 * 用户记忆卡表(MemoryCard)表服务接口
 *
 * @author makejava
 * @since 2023-03-18 14:47:40
 */
public interface MemoryCardService extends IService<MemoryCard> {

    AjaxResult createMemoryCard(MemoryCardDto dto, HttpServletRequest request);
}


