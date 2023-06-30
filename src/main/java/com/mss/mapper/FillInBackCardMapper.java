package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.FillInBackCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 填空表(FillInBackCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-20 21:46:31
 */
@Mapper
public interface FillInBackCardMapper extends BaseMapper<FillInBackCard> {

}
