package com.mss.controller;


import com.mss.domain.dto.DynamicsCommentDto;
import com.mss.domain.dto.ReplyCommentDto;
import com.mss.domain.dto.UserDynamicsDto;
import com.mss.domain.dto.UserDynamicsDto1;
import com.mss.domain.entity.EverydaySentence;
import com.mss.domain.entity.TodayEssay;
import com.mss.domain.entity.UserDynamics;
import com.mss.service.EverydaySentenceService;
import com.mss.service.OssService;
import com.mss.service.TodayEssayService;
import com.mss.service.UserDynamicsService;
import com.mss.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Api(tags = "发现")
@RestController
@RequestMapping("/community")
public class DiscoverController {

    @Autowired
    private EverydaySentenceService sentenceService;

    @Autowired
    private TodayEssayService essayService;

    @Lazy
    @Autowired
    private UserDynamicsService DynamicsService;


//
    @ApiOperation("获取每日一句")
    @GetMapping("/everydaySentence")
    public AjaxResult getEveryDaySentence(){



        long time = new Date().getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String date = dateFormat.format(time);

        int intValue = new Integer(date).intValue();

        EverydaySentence sentence = sentenceService.getById(intValue);
        return AjaxResult.success(sentence);

    }

    @ApiOperation("添加每日一句")
    @GetMapping("/addEverydaySentence")
    public AjaxResult addEveryDaySentence(EverydaySentence everydaySentence){


        sentenceService.save(everydaySentence);
        return AjaxResult.success();

    }


//    @ApiOperation("获取今日短文")
//    @GetMapping("/todayEssay")
//    public AjaxResult getTodayEssay(){
//        TodayEssay essay = essayService.getById(1);
//        return AjaxResult.success(essay);
//    }


    //已经完成
    @ApiOperation("获取社区动态列表")
    @GetMapping("/userDynamicsList")
    public AjaxResult getUserDynamicsList(HttpServletRequest request){

        return DynamicsService.getUserDynamicsList(request);

    }


//    @ApiOperation("用户发布动态，需要token")
//    @PostMapping("/publish/dynamics")
//    public AjaxResult publishDynamics(UserDynamicsDto dynamics,
//                                      @RequestParam(value ="file",required = false)List<MultipartFile> files,
//                                      HttpServletRequest request){
//
//        return DynamicsService.publishDynamics(dynamics,files,request);
//
//    }


    @ApiOperation("用户发布动态，需要token,有照片")
    @PostMapping("/publish/NewDynamics")
    public AjaxResult publishDynamicss(UserDynamicsDto dynamics,
                                       Integer number,
                                       MultipartRequest files,
                                       HttpServletRequest request){

        return DynamicsService.publishDynamicss(dynamics,number,files,request);

    }

    @ApiOperation("用户发布动态，需要token,无照片")
    @PostMapping("/publish/NewDynamicsNoPictrue")
    public AjaxResult publishDynamicsNoPictrue(@RequestBody UserDynamicsDto dynamics,
                                       HttpServletRequest request){

        return DynamicsService.publishDynamicsNoPictrue(dynamics,request);

    }


//    @ApiOperation("用户发布动态，要token,新新接口")
//    @PostMapping("/publish/adynamics")
//    public AjaxResult publishDynamicss(@RequestBody UserDynamicsDto1 dynamics,
//                                       @RequestPart(value = "file",required = false) MultipartRequest files,
//                                       HttpServletRequest request){
//
//        return DynamicsService.publishNewDynamicss(dynamics,files,request);
//
//    }

    @ApiOperation("根据id查看具体动态的详情")
    @GetMapping("/lookDynamicsDetailById/{id}")
    public AjaxResult lookDynamicsDetailById(@PathVariable("id")Long id,
                                             HttpServletRequest request)
    {

        return DynamicsService.lookDynamicsDetailById(id,request);

    }


//    @ApiOperation("根据token查询用户的动态列表,在个人中心页面回显")
//    @GetMapping("/getDynamicsListById")
//    private AjaxResult getDynamicsListById(HttpServletRequest request){
//        return DynamicsService.getDynamicsListByUserId(request);
//    }


    @ApiOperation("根据动态id删除动态")
    @DeleteMapping("/deleteDynamicsById/{id}")
    private AjaxResult deleteDynamicsById(@PathVariable("id") Long id){
        return  DynamicsService.deleteDynamicsById(id);
    }

    @ApiOperation("用户点赞社区动态")
    @PutMapping("likeDynamics/{dynamicsId}")
    public AjaxResult likeDynamics(@PathVariable("dynamicsId") Long dynamicsId,
                                   HttpServletRequest request){


        return DynamicsService.likeDynamics(dynamicsId,request);



    }

    @ApiOperation("用户点赞评论")
    @PutMapping("/likeComment/{commentId}")
    public AjaxResult likeComment(@PathVariable("commentId") Long id,
                                  HttpServletRequest request){

        return DynamicsService.likeComment(id,request);

    }


    @ApiOperation("就某一篇动态发表评论,动态评论可以发图片")
    @PostMapping("/publishComment")
    public AjaxResult publishComment(@RequestBody DynamicsCommentDto dto,
                                     @RequestParam(required = false)Integer number,
                                     @RequestParam(required = false)MultipartRequest files,
                                     HttpServletRequest request  ){


        return DynamicsService.publishComment(dto,number,files,request);

    }


    @ApiOperation("回复动态中的某一条评论,只能回复文字")
    @PostMapping("/replyComment")
    public AjaxResult replyComment(@RequestBody(required = true)ReplyCommentDto dto,
                                   HttpServletRequest request){

        return DynamicsService.replyComment(dto,request);

    }


    @ApiOperation("根据动态的id查看该动态的评论列表")
    @GetMapping("/lookDynamicsComment/{id}")
    public AjaxResult lookDynamicsComment(@PathVariable("id") Long Id,
                                          HttpServletRequest request){

        return DynamicsService.lookComment(Id,request);

    }


    @ApiOperation("根据评论id查看该一级评论的子评论列表")
    @GetMapping("/lookCommentListDeatils/{id}")
    private AjaxResult lookCommentListDeatilsById(@PathVariable("id")Long id,
                                              HttpServletRequest request){

        return DynamicsService.lookChildCommentDetails(id,request);

    }

    @ApiOperation("根据评论id查看该一级评论的评论详情")
    @GetMapping("/lookCommentDeatils/{id}")
    private AjaxResult lookCommentDeatilsById(@PathVariable("id")Long id,
                                              HttpServletRequest request){

        return DynamicsService.lookCommentDetails(id,request);

    }







}
