package com.mss.service.impl;

import cn.hutool.core.date.DateTime;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.entity.UserDynamics;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.service.OssService;
import com.mss.constant.OosConstant;
import com.mss.service.OssService;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import org.omg.CORBA.portable.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    @Override
    public String uploadFileAvatar(MultipartFile file) {

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;
        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            // yuy76t5rew01.jpg
            fileName = uuid+fileName;

            //2 把文件按照日期进行分类
            //获取当前日期
            //   2019/11/12
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            fileName = datePath+"/"+fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName,fileName , inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AjaxResult updatePicture(UserDynamicsDto dto, MultipartFile file)  {


        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        System.out.println(dto.toString());

        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        InputStream inputStream=null;
        //获取上传文件输入流
        try {

            inputStream = file.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取文件名称
        String fileName = file.getOriginalFilename();

        if(inputStream==null){
        System.out.println("文件为空！");
        throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        /**
         * 下面两行代码是重点坑：
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接，而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();

        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileName, inputStream);

        meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        meta.setObjectAcl(CannedAccessControlList.PublicRead);
        objectRequest.setMetadata(meta);
        objectRequest.setProcess("ture");

        PutObjectResult result = ossClient.putObject(objectRequest);
//        ossClient.putObject(bucketName,fileName,inputStream,meta);

        // 关闭OSSClient。
        ossClient.shutdown();

        String uri = result.getResponse().getUri();
        System.out.println(uri);

        //把上传之后文件路径返回
        //需要把上传到阿里云oss路径手动拼接出来
        //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
        String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
        return AjaxResult.success(uri);


    }


//    上传多文件
    @Override
    public AjaxResult updatePictures(List<MultipartFile> files){

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        System.out.println(files);


        System.out.println(endpoint);
        System.out.println(accessKeyId);
        System.out.println(accessKeySecret);
        System.out.println(OosConstant.BUCKET_NAME);
        System.out.println(OosConstant.ACCESS_KEY_ID);
        System.out.println(OosConstant.ACCESS_KEY_SECRET);


        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

//
       StringBuilder urls=new StringBuilder();

        InputStream inputStream=null;
        for(MultipartFile file : files){
//
            try {
                inputStream= file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //获取文件名称
            String fileName = file.getOriginalFilename();

            if(inputStream==null){
                System.out.println("文件为空！");
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
            ObjectMetadata meta = new ObjectMetadata();

            meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            meta.setObjectAcl(CannedAccessControlList.PublicRead);

            ossClient.putObject(bucketName,fileName,inputStream,meta);

            String url = "https://"+bucketName+"."+endpoint+"/"+fileName+"<<image>>";

            urls.append(url);
        }


//        UserDynamics userDynamics = BeanCopyUtils.copyBean(dto, UserDynamics.class);

//        userDynamics.setCreateBy();

        // 关闭OSSClient。
        ossClient.shutdown();

        return AjaxResult.success(urls);

    }


    /**
     * 上传图片数组
     * @param files
     * @return
     */
    @Override
    public String updateImages(List<MultipartFile> files) {

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        StringBuilder urls = new StringBuilder();

        InputStream inputStream = null;

//        判断用户是否上传图片
        if (!Objects.isNull(files))
            for (MultipartFile file : files) {
//
                try {
                    inputStream = file.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //获取文件名称
                String fileName = file.getOriginalFilename();

                if (inputStream == null) {
                    System.out.println("文件为空！");
                    throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                }
                ObjectMetadata meta = new ObjectMetadata();

                meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
                meta.setObjectAcl(CannedAccessControlList.PublicRead);

                ossClient.putObject(bucketName, fileName, inputStream, meta);

                String url = "https://" + bucketName + "." + endpoint + "/" + fileName + "<image>";

                urls.append(url);

            }

        return urls.toString();

    }

    /**
     * 上传音频文件
     * @param file
     * @return
     */
    @Override
    public String uploadAudiofiles(File file) {

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        FileOutputStream outputStream=null;
        FileInputStream inputStream=null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //获取上传文件输入流

        //获取文件名称
        String fileName = file.getName();

        if(outputStream==null){
            System.out.println("文件为空！");
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        /**
         * 下面两行代码是重点坑：
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接，而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();

        PutObjectRequest objectRequest = new
                PutObjectRequest(bucketName, fileName, inputStream);

        meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        meta.setObjectAcl(CannedAccessControlList.PublicRead);
        objectRequest.setMetadata(meta);
        objectRequest.setProcess("ture");

        PutObjectResult result = ossClient.putObject(objectRequest);
        ossClient.putObject(bucketName,fileName,inputStream,meta);

        // 关闭OSSClient。
        ossClient.shutdown();

        //把上传之后文件路径返回
        //需要把上传到阿里云oss路径手动拼接出来
        //  https://edu-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
        String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
        return url;

    }


    /**
     * 上传文件，音频视频图片
     * @param file
     * @return
     */
    @Override
    public AjaxResult uploadFile(MultipartFile file) {

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件名称
        String fileName = file.getOriginalFilename();


        if (inputStream == null) {
            System.out.println("文件为空！");
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        ObjectMetadata meta = new ObjectMetadata();

        meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        meta.setObjectAcl(CannedAccessControlList.PublicRead);

        ossClient.putObject(bucketName, fileName, inputStream, meta);

        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

        System.out.println(url);

        return AjaxResult.success().put("path", url);

    }

    /**
     * 上传文件(文件包括视频，音频和图片）到阿里云oss,返回文件的线上地址
     * @param file
     * @return
     */
    @Override
    public String uploadbaseFile(MultipartFile file) {

        String endpoint = OosConstant.END_POIND;
        String accessKeyId = OosConstant.ACCESS_KEY_ID;
        String accessKeySecret = OosConstant.ACCESS_KEY_SECRET;
        String bucketName = OosConstant.BUCKET_NAME;

        // 创建OSS实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        InputStream inputStream = null;

        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文件名称
        String fileName = file.getOriginalFilename();


        if (inputStream == null) {
            System.out.println("文件为空！");
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        ObjectMetadata meta = new ObjectMetadata();

        meta.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
        meta.setObjectAcl(CannedAccessControlList.PublicRead);

        ossClient.putObject(bucketName, fileName, inputStream, meta);

        String url = "https://" + bucketName + "." + endpoint + "/" + fileName;

        return url;

    }


    /**
     * 删除文件，根据文件名
     * @param filename
     * @return
     */
    @Override
    public AjaxResult deleteFile(String filename) {
        return null;
    }


    //通过该方法快速获取文件类型
    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

}

