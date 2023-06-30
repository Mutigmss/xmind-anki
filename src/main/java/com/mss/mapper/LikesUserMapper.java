package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.LikesUser;
import org.apache.ibatis.annotations.Mapper;


/**
 * 点赞关联表(LikesUser)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-10 12:54:02
 */
@Mapper
public interface LikesUserMapper extends BaseMapper<LikesUser> {

}
