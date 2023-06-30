package com.mss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.domain.entity.Comment;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.mapper.CommentMapper;
import com.mss.service.CommentService;
import com.mss.service.UserDynamicsService;
import com.mss.utils.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 12:52:12
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {


    @Autowired
    private UserDynamicsService userDynamicsService;

    @Autowired
    private CommentService commentService;

    /**
     * 根据token查询用户的评论列表,在个人中心页面回显
     * @param request
     * @return
     */
    @Override
    public AjaxResult getCommentListByUserId(HttpServletRequest request) {

        return null;
    }



    /**
     * 根据评论id删除评论
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteCommentById(Long id) {


        Comment comment = getById(id);

        if(comment.getType().equals("0")){

//            修改动态一级评论数量
           boolean issuccess = userDynamicsService.update()
                   .setSql("comment_number=comment_number -1")
                   .eq("id", comment.getToCommentId()).update();


        }else if(comment.getType().equals("1")){

//            修改动态中的评论的评论数量
            boolean issuccess = commentService.update().
                    setSql("comment_number=comment_number -1")
                    .eq("id", comment.getToCommentId()).update();

        }


        boolean issuccess = removeById(id);

        if( !issuccess ){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return AjaxResult.success();
    }



}

