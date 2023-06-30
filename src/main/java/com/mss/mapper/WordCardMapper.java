package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.WordCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 智能单词卡表(WordCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-23 12:17:08
 */
@Mapper
public interface WordCardMapper extends BaseMapper<WordCard> {

}
