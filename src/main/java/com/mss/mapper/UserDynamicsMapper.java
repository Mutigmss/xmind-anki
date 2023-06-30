package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.UserDynamics;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户动态表(UserDynamics)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-10 12:54:52
 */
@Mapper
public interface UserDynamicsMapper extends BaseMapper<UserDynamics> {

}
