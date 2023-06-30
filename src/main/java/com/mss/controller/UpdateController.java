package com.mss.controller;

//import com.baomidou.mybatisplus.extension.api.R;
import com.mss.domain.Result;
import com.mss.service.OssService;
import com.mss.service.SysUserService;
import com.mss.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "上传文件")
@RestController
public class UpdateController {


    @Autowired
    private SysUserService service;

    @Autowired
    private OssService oosService;

    @ApiOperation("更新用户头像")
    @PostMapping("/updateAvatar")
    public Result updateAvatar(MultipartFile file){
        final String url = oosService.uploadFileAvatar(file);
        System.out.println(url);
        return Result.ok(url);
    }

    @ApiOperation("富文本上传文件(音频，视频，图片),一份一份上传")
    @PostMapping("/uploadFile")
    private String UpdateFile(@RequestParam("file") MultipartFile file){

        return oosService.uploadbaseFile(file);

    }

    @ApiOperation("上传文件")
    @PostMapping("/uploadFiles")
    private AjaxResult uploadFiles(MultipartFile file){

        return oosService.uploadFile(file);

    }


}
