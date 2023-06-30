package com.mss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mss.domain.dto.ChildMemoryBoxDto;
import com.mss.domain.dto.MemoryBoxDto;
import com.mss.domain.dto.UpdateMemoryBoxDto;
import com.mss.domain.entity.MemoryBox;
import com.mss.utils.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户闪记盒(MemoryBox)表服务接口
 *
 * @author makejava
 * @since 2023-03-18 14:48:27
 */
public interface MemoryBoxService extends IService<MemoryBox> {

   AjaxResult updateBox(UpdateMemoryBoxDto dto, MultipartFile file) ;

    AjaxResult createMemoryBox(MemoryBoxDto dto, MultipartFile file, HttpServletRequest request);

    AjaxResult getUserMemoryBoxList(HttpServletRequest request);

    AjaxResult createChildBox(ChildMemoryBoxDto dto, HttpServletRequest request);

    AjaxResult getBoxDetailsByBoxId(Long id);

    AjaxResult createCardBox(MemoryBoxDto dto, HttpServletRequest request);

    AjaxResult getUserMemoryBoxTree(HttpServletRequest request);

    AjaxResult stopLearnBoxById(Long id);

    AjaxResult resetBoxById(Long id,HttpServletRequest request);
}


