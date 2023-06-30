package com.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.domain.entity.EverydaySentence;
import com.mss.mapper.EverydaySentenceMapper;
import com.mss.service.EverydaySentenceService;
import org.springframework.stereotype.Service;

/**
 * 每日一句表(EverydaySentence)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 18:02:10
 */
@Service("everydaySentenceService")
public class EverydaySentenceServiceImpl extends ServiceImpl<EverydaySentenceMapper, EverydaySentence> implements EverydaySentenceService {

}

