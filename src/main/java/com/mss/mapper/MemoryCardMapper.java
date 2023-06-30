package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.MemoryCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户记忆卡表(MemoryCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-18 14:47:40
 */
@Mapper
public interface MemoryCardMapper extends BaseMapper<MemoryCard> {

}
