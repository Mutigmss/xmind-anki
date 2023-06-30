package com.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.domain.entity.LikesUser;
import com.mss.mapper.LikesUserMapper;
import com.mss.service.LikesUserService;
import org.springframework.stereotype.Service;

/**
 * 点赞关联表(LikesUser)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 12:54:02
 */
@Service("likesUserService")
public class LikesUserServiceImpl extends ServiceImpl<LikesUserMapper, LikesUser> implements LikesUserService {

}

