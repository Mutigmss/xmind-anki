package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.ReplyCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.dto.UserDynamicsDto1;
import com.mss.domain.entity.UserDynamics;
import com.mss.utils.AjaxResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 用户动态表(UserDynamics)表服务接口
 *
 * @author makejava
 * @since 2023-03-10 12:54:52
 */
public interface UserDynamicsService extends IService<UserDynamics> {

    AjaxResult publishDynamics(UserDynamicsDto dynamics,
                               List<MultipartFile> files,HttpServletRequest request);


    AjaxResult publishDynamicss(UserDynamicsDto dynamics, Integer number,
                                MultipartRequest filerequest, HttpServletRequest request);

    AjaxResult getUserDynamicsList(HttpServletRequest request);

    AjaxResult likeDynamics(Long dynamicsId,HttpServletRequest request);

    AjaxResult likeComment(Long id, HttpServletRequest request);

    AjaxResult lookComment(Long id,HttpServletRequest request);

    AjaxResult publishComment(DynamicsCommentDto dto,
                              Integer number,
                              MultipartRequest files,
                              HttpServletRequest request   );

    AjaxResult replyComment(ReplyCommentDto dto, HttpServletRequest request);

    AjaxResult lookDynamicsDetailById(Long id,HttpServletRequest request);


    AjaxResult getDynamicsListByUserId(HttpServletRequest request);

    AjaxResult deleteDynamicsById(Long id);


    AjaxResult lookChildCommentDetails(Long id,HttpServletRequest request);

    AjaxResult lookCommentDetails(Long id, HttpServletRequest request);

    AjaxResult publishNewDynamicss(UserDynamicsDto1 dynamics, MultipartRequest files, HttpServletRequest request);

    AjaxResult publishDynamicsNoPictrue(UserDynamicsDto dynamics,  HttpServletRequest request);
}


