package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.OptionCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户笔记卡表(OptionCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-23 12:16:40
 */
@Mapper
public interface OptionCardMapper extends BaseMapper<OptionCard> {

}
