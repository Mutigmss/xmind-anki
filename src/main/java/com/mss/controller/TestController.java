package com.mss.controller;


import com.mss.chatgptclient.ChatGPTClient;
import com.mss.chatgptdemo.CustomChatGpt;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.entity.SysUser;
import com.mss.domain.entity.UserDynamics;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.service.OssService;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Api(tags = "测试接口")
@RestController
public class TestController {

    @Autowired
    private OssService ossService;


    @ApiOperation("测试富文本")
    @GetMapping("/send/fwb")
    private AjaxResult testfwb(@RequestParam("htmltext") String html){
        System.out.println(html);
        System.out.println("-------------------------------------");
        return AjaxResult.success();
    }


    @ApiOperation("测试uni.request")
    @GetMapping("/testGet")
    private AjaxResult testGet(){
        return AjaxResult.success("hhhhhh,这是测试uni.request方法！");
    }



    @ApiOperation("测试chatgpt,国内代理接口")
    @PostMapping("/testchatgpt")
    private AjaxResult testchatgpt(@RequestBody String question)
    {


        CloseableHttpClient httpClient = HttpClients.createDefault();

//        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    没有使用额度了
//        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   没有使用额度了
//        fk-o5gBen369d-D0ndZ_PlxsDNPxjq5z7u3bbgn1d9e63k
//        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

        String apiKey = "fk-o5gBen369d-D0ndZ_PlxsDNPxjq5z7u3bbgn1d9e63k";
        CustomChatGpt customChatGpt = new CustomChatGpt(apiKey);
        // 根据自己的网络设置吧
        customChatGpt.setResponseTimeout(55000);

        long start = System.currentTimeMillis();
        String answer = customChatGpt.getAnswer(httpClient, question);
        long end = System.currentTimeMillis();
        System.out.println("该回答花费时间为：" + (end - start) / 1000.0 + "秒");
        System.out.println(answer);

//        return AjaxResult.success().put("answer", answer)
//                .put("needTime", (end - start) / 1000.0 + "秒");

        return AjaxResult.success(answer);

    }


    @ApiOperation("测试上传多种照片")
    @PostMapping("/testSendImage")
    public AjaxResult publishDynamicss(String content,
                                       Integer number,
                                       MultipartRequest filerequest) {

//        创建图片二进制接收器数组
        List<MultipartFile> files = new ArrayList<>();

        System.out.println(number);
        System.out.println(filerequest);

//        把接收到的二进制图片添加进入接收器数组
        if(number!=null)
            if (number >0) {

                if(Objects.isNull(filerequest)){
                    return AjaxResult.error("File request is null");
                }
                for (int i = 0; i < number; i++) {

                    MultipartFile file = filerequest.getFile("file" + i);
                    if(!Objects.isNull(file)){
                        files.add(file);
                    }else{
                        throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
                    }

                }
            }


//        上传图片，返回图片地址
        String images = ossService.updateImages(files);


        if(!Objects.isNull(images)){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }


    }

}
