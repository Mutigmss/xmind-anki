package com.mss.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.entity.ClockIn;
import com.mss.domain.entity.SysUser;
import com.mss.domain.vo.ClockInVo;
import com.mss.mapper.ClockInMapper;
import com.mss.service.ClockInService;
import com.mss.utils.AjaxResult;
import com.mss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学习打卡表(ClockIn)表服务实现类
 *
 * @author makejava
 * @since 2023-03-26 22:52:25
 */
@Service("clockInService")
public class ClockInServiceImpl extends ServiceImpl<ClockInMapper, ClockIn> implements ClockInService {




    @Autowired
    private RedisCache redisCache;


    /**
     *
     * 根据用户id查询打卡记录,一个月
     * @param request
     * @return
     */
    @Override
    public AjaxResult getClockInRecordByUserId(HttpServletRequest request) {


        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


        Date date = new Date();

//        yyyy-MM-dd HH:mm:ss
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

        String format = dateFormat.format(date);
        String substring = format.substring(5, 7);
        int i = Integer.parseInt(substring);
        String s = String.valueOf(i);


//        获取当月天数
        Calendar calendar = new GregorianCalendar();
        int alldays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<ClockIn> lists = list(new QueryWrapper<ClockIn>().eq("user_id", user.getId()));

        Date date1 = new DateTime(format + "-01 00:00:00");
        Date date2 = new DateTime(format +"-"+alldays+" 23:59:59");

//      筛选出该月的打卡记录
        lists=lists.stream()
                .filter(a->a.getClockTime().before(date2))
                .filter(b->b.getClockTime().after(date1))
                .collect(Collectors.toList());

//        ArrayList<Integer> list = new ArrayList<>();

        HashMap<Integer, Boolean> map = new HashMap<>();
//
        for (ClockIn a:lists) {

//            list.add(Integer.valueOf(
//                    new SimpleDateFormat("MM").format(a.getClockTime())));

            map.put(Integer.valueOf(
                    new SimpleDateFormat("MM").format(a.getClockTime())),true);

        }

//        判断该月的每一天，那天打卡了，若打卡则返回该日为true
        boolean[] booleans = new boolean[alldays];
        for (i=0;i<alldays;i++) {

            if(!Objects.isNull(map.get(i+1))){
                booleans[i]=true;
            }

        }


        ClockInVo vo = new ClockInVo(
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                alldays,booleans);

        return AjaxResult.success(vo);
    }


    /**
     * 用户手动打卡
     * @param request
     * @return
     */
    @Override
    public AjaxResult userClockIn(HttpServletRequest request) {


        //解析请求头,获取redis中登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        ClockIn clockIn = new ClockIn();
        clockIn.setUserId(user.getId());
        save(clockIn);

        return AjaxResult.success();
    }
}

