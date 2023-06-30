package com.mss.controller;

import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.service.OssService;
import com.mss.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(tags = "阿里云oos测试")
@RestController
@RequestMapping("/publish")
public class PublishController {


    @Autowired
    private OssService ossService;

    /**
     * 上传一张照片
     * @param files
     * @return
     */
    @ApiOperation("上传一张照片和文字")
    @PostMapping("/picture")
    public AjaxResult updatePicture(UserDynamicsDto f, @RequestParam("file") MultipartFile files){

        return ossService.updatePicture(f,files);

    }

    /**
     * 上传多张照片
     * @param files
     * @return
     */
    @ApiOperation("上传多张照片")
    @PostMapping("/pictures")
    public AjaxResult updatePictures(@RequestParam("file")List<MultipartFile> files){

        return ossService.updatePictures(files);

    }

}
