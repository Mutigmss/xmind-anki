package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.entity.UserLearn;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户学习卡片次数表（学习卡片的次数，相同卡片可以重复学习）(UserLearn)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 15:54:23
 */
@Mapper
public interface UserLearnMapper extends BaseMapper<UserLearn> {

}
