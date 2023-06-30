package com.mss.service;

import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.utils.AjaxResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface OssService {
    //上传头像到oss
    String uploadFileAvatar(MultipartFile file);

//    上传一张图片
    AjaxResult updatePicture(UserDynamicsDto f , MultipartFile file);

    AjaxResult updatePictures(List<MultipartFile> files);

//    AjaxResult updatePicture(MultipartFile files);

    String updateImages(List<MultipartFile> files);

    String uploadAudiofiles(File file);

//    上传文件，富文本编辑
    AjaxResult uploadFile(MultipartFile file);


//    上传文件(音频，视频，图片)，返回上传后的地址

    String uploadbaseFile(MultipartFile file);

//    删除文件
    AjaxResult deleteFile(String filename);

}
