package com.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.domain.entity.TodayEssay;
import com.mss.mapper.TodayEssayMapper;
import com.mss.service.TodayEssayService;
import org.springframework.stereotype.Service;

/**
 * 今日短文表(TodayEssay)表服务实现类
 *
 * @author makejava
 * @since 2023-03-12 18:05:23
 */
@Service("todayEssayService")
public class TodayEssayServiceImpl extends ServiceImpl<TodayEssayMapper, TodayEssay> implements TodayEssayService {

}

