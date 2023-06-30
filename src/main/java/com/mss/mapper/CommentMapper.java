package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-10 12:52:08
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}
