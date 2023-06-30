package com.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.entity.UserLearn;
import com.mss.mapper.UserLearnMapper;
import com.mss.service.UserLearnService;
import org.springframework.stereotype.Service;

/**
 * 用户学习卡片次数表（学习卡片的次数，相同卡片可以重复学习）(UserLearn)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 15:54:23
 */
@Service("userLearnService")
public class UserLearnServiceImpl extends ServiceImpl<UserLearnMapper, UserLearn> implements UserLearnService {

}

