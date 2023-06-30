package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.entity.Comment;
import com.mss.utils.AjaxResult;

import javax.servlet.http.HttpServletRequest;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-03-10 12:52:12
 */
public interface CommentService extends IService<Comment> {

    AjaxResult getCommentListByUserId(HttpServletRequest request);

    AjaxResult deleteCommentById(Long id);
}


