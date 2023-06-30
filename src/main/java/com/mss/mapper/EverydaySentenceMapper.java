package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.EverydaySentence;
import org.apache.ibatis.annotations.Mapper;


/**
 * 每日一句表(EverydaySentence)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-12 18:02:10
 */
@Mapper
public interface EverydaySentenceMapper extends BaseMapper<EverydaySentence> {

}
