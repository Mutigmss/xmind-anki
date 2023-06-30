package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.MemoryBox;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户闪记盒(MemoryBox)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-18 14:48:27
 */
@Mapper
public interface MemoryBoxMapper extends BaseMapper<MemoryBox> {

}
