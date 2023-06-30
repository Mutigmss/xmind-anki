package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.FrontBackCard;
import org.apache.ibatis.annotations.Mapper;


/**
 * 正反卡表(FrontBackCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-25 18:46:56
 */
@Mapper
public interface FrontBackCardMapper extends BaseMapper<FrontBackCard> {

}
