package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.ClockIn;
import org.apache.ibatis.annotations.Mapper;


/**
 * 学习打卡表(ClockIn)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-26 22:52:25
 */
@Mapper
public interface ClockInMapper extends BaseMapper<ClockIn> {

}
