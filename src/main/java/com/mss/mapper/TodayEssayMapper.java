package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.TodayEssay;
import org.apache.ibatis.annotations.Mapper;


/**
 * 今日短文表(TodayEssay)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 18:05:23
 */
@Mapper
public interface TodayEssayMapper extends BaseMapper<TodayEssay> {

}
