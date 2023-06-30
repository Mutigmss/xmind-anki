package com.mss.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mss.constant.TokenConstant;
import com.mss.domain.entity.ClockIn;
import com.mss.domain.entity.PublicCard;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.LearnDataVo;
import com.mss.domain.vo.UserInfoVo;
import com.mss.entity.UserLearn;
import com.mss.mapper.PublicCardMapper;
import com.mss.service.*;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import com.mss.utils.RedisCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "我的")
@RestController
@RequestMapping("/my")
public class MyController {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private CommentService commentService;

    @Autowired
    private ClockInService clockInService;


    @Autowired
    private UserLearnService userLearnService;

    @Autowired
    private PublicCardService publicCardService;

    @Autowired
    private PublicCardMapper publicCardMapper;

    /**
     * 获取用户信息(头像，邮箱，手机号码，用户名，昵称)
     * @param Request
     * @return
     */
    @ApiOperation(value = "获取用户信息,需要前端传递token")
    @GetMapping("/getUserInfo")
    private AjaxResult getInfo(HttpServletRequest Request){

//      解析请求头
        String token = Request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        SysUser user1 = sysUserService.getById(user.getId());

//      返回前端需要的用户信息vo封装类
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user1, UserInfoVo.class);
        return AjaxResult.success(userInfoVo);

    }

    @ApiOperation("修改用户信息,需要前端传递token")
    @PostMapping("updateUserInfo")
    private AjaxResult updateUserInfo(@RequestBody UserInfoVo userInfoVo,
                                      @RequestParam(value = "file",required = false)MultipartFile file,
                                      HttpServletRequest request){

        return sysUserService.updateUserInfo(userInfoVo,file,request);

    }

    @ApiOperation("更新用户头像,需要token")
    @PostMapping("/updateAvatar")
    private AjaxResult updateAvatar(MultipartRequest file,Integer number,
                                    HttpServletRequest request){

        return  sysUserService.updateUserAvatar(file,number,request);

    }


//    @ApiOperation("更新用户名,需要token")
//    @PostMapping("/updateUsername")
//    private AjaxResult updateUsername(String username,HttpServletRequest request){
//
//        return sysUserService.updateusername(username,request);
//
//    }


//@ApiOperation("根据token查询用户的评论列表,在个人中心页面回显")
//@GetMapping("/getCommentListById")
//private AjaxResult getCommentListById(HttpServletRequest request){
//    return commentService.getCommentListByUserId(request);
//}



//    @ApiOperation("根据评论id删除动态")
//    @DeleteMapping("/deleteCommentById/{id}")
//    private AjaxResult deleteCommentById(@PathVariable("id") Long id){
//        return commentService.deleteCommentById(id);
//    }





    @ApiOperation("根据用户id查询打卡记录,该月打卡一览表，需要token")
    @GetMapping("/getClockInRecord")
    private AjaxResult getClockInRecord(HttpServletRequest request){


        return clockInService.getClockInRecordByUserId(request);

    }



    @ApiOperation("用户学习打卡,手动打卡接口")
    @GetMapping("/userClockIn")
    private AjaxResult userClockIn(HttpServletRequest request){


        return clockInService.userClockIn(request);

    }


    @ApiOperation("获取学习统计数据接口")
    @GetMapping("/getLearnStatisticalData")
    private AjaxResult getLearnStatisticalData(HttpServletRequest request){

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//        获取累计学习卡牌数
        List<UserLearn> list = userLearnService.list(
                new LambdaQueryWrapper<UserLearn>()
                        .eq(UserLearn::getUserId, user.getId()));

//        卡片学习次数learncount,相同卡片可以重复学习
        int learncount = list.size();

        for (UserLearn u : list)
        {
            System.out.println(u);
        }
//        获取连续打卡天数（打卡方式为手动打卡）
//        查询打卡表，获取该用户id的所有该月的打卡记录，如何获取该月从一日到现在的连续打卡天数

//        List<ClockIn> clockList = clockInService.list(
//                new QueryWrapper<ClockIn>().eq("user_id", user.getId()));
//
//        List<String> collect = clockList.stream()
//                .map(a->new SimpleDateFormat("yyyy-MM-dd").format(a.getClockTime()))
//                .collect(Collectors.toList());
//
//        List<Date> signInDates = new ArrayList<>();
//        try {
//            for (String dateStr : collect) {
//                Calendar calendarTo = Calendar.getInstance();
//                calendarTo.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
//                Calendar c = Calendar.getInstance();
//                c.setTimeInMillis(0);
//                c.set(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH),
//                        calendarTo.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//                signInDates.add(c.getTime());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
////      连续签到数ContinuousClockInNumber
//        int ContinuousClockInNumber = persistentDay(signInDates);
//        System.out.println("已连续签到 " + ContinuousClockInNumber + "天");

//      获取用户创建卡片数
//        List<PublicCard> cardList = publicCardService.list(

//                new QueryWrapper<PublicCard>().eq("user_id", user.getId()));
        List<PublicCard> cardList = publicCardMapper.searchCardByUserId(user.getId());
//      用户卡片数量cardCount
        int cardCount = cardList.size();

        int masterCount = cardList.stream().filter(card -> card.getLearnCount() >= 6).collect(Collectors.toList()).size();

        int count = cardList.stream().filter(card -> card.getLearnCount() >0&&card.getLearnCount()<6).collect(Collectors.toList()).size();


        LearnDataVo dataVo = new LearnDataVo(new Integer(learncount),count,  new Integer(masterCount),cardCount);

//        返回统计数据显示框所需数据(学习卡片次，卡牌累计学习数量，掌握数量,全部卡牌数量)
        return AjaxResult.success(dataVo);

    }

    /**
     * 需要手动点击签到的情况
     * @param signInDates
     * @return
     */
    private static int persistentDay(List<Date> signInDates) {
        //定义一个变量表示连续签到天数，从1开始
        int continuousDays = 1;

        /**
         * 如果手动签到的话需要考虑
         * 把排序之后的签到记录时间中最大的那个时间拿出来与 昨天 进行比较，如果相等证明还是连续签到的，如果不等则连续签到变成0
         */
        Calendar yesterday = Calendar.getInstance();
        yesterday.setTime(new Date());
        yesterday.add(Calendar.DAY_OF_MONTH, -1);

        Calendar lastDay = Calendar.getInstance();
        lastDay.setTime(signInDates.get(signInDates.size() - 1));
        if (yesterday.get(Calendar.YEAR) != lastDay.get(Calendar.YEAR)
                || yesterday.get(Calendar.MONTH) != lastDay.get(Calendar.MONTH)
                || yesterday.get(Calendar.DAY_OF_YEAR) != lastDay.get(Calendar.DAY_OF_YEAR)) {
            //昨天没有签到
            continuousDays = 0;
            return continuousDays;
        }

        /**
         * 2. 从最大的时间开始往前比较，因为我们是要拿连续签到的时间，这样才有意义，减少无谓的比较
         */
        Calendar later = Calendar.getInstance();
        Calendar before = Calendar.getInstance();
        for (int i = signInDates.size() - 1; i > 0; i--) {
            later.setTime(signInDates.get(i));
            before.setTime(signInDates.get(i - 1));
            //前一天 + 1天 = 后一天，则视为连续签到
            before.add(Calendar.DAY_OF_MONTH, 1);
            if (later.get(Calendar.YEAR) == before.get(Calendar.YEAR)
                    && later.get(Calendar.MONTH) == before.get(Calendar.MONTH)
                    && later.get(Calendar.DAY_OF_YEAR) == before.get(Calendar.DAY_OF_YEAR)) {
                continuousDays++;
            } else {
                //只要遇到不连续的就不用再往前比较了
                break;
            }
        }
        return continuousDays;
    }


}
