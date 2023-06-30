package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.NoteCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户笔记卡表(NoteCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-21 17:22:38
 */
@Mapper
public interface NoteCardMapper extends BaseMapper<NoteCard> {

}
