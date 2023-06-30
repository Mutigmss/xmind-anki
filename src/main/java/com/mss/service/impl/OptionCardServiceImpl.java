package com.mss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.OptionCardDto;
import com.mss.domain.entity.*;
import com.mss.mapper.OptionCardMapper;
import com.mss.service.MemoryBoxService;
import com.mss.service.OptionCardService;
import com.mss.service.PublicCardService;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import com.mss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 用户笔记卡表(OptionCard)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:16:40
 */
@Service("optionCardService")
public class OptionCardServiceImpl extends ServiceImpl<OptionCardMapper, OptionCard> implements OptionCardService {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MemoryBoxService memoryBoxService;


    @Autowired
    private PublicCardService publicCardService;


    /**
     * 创建选项卡
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult createOptionCard(OptionCardDto dto, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        OptionCard card = BeanCopyUtils.copyBean(dto, OptionCard.class);
        card.setUserId(user.getId());

        MemoryBox box = memoryBoxService.getOne( new LambdaQueryWrapper<MemoryBox>()
                .eq(MemoryBox::getId, dto.getBelongToBoxId()));
//               .eq(MemoryBox::getBoxName,dto.getBelongToBoxName()));

//        如果该卡片的根盒子是卡片盒，则在该卡牌盒下创建一级文件夹（盒子），文件夹的初始名字为创建卡片的日期，
//        该一级文件夹中放卡片
        if(box.getRootId().longValue()==0){

            MemoryBox memoryBox = new MemoryBox();
            memoryBox.setRootId(box.getId());

            Date ss = new Date();

//          先创建卡片盒下的一级文件夹（盒子）
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String time = format.format(ss.getTime());

            //判断是否已经存在一个名字为time的盒子，若存在则新建的卡牌放到该盒子下，无需再根据当天时间创建一个新盒子
            MemoryBox box1=memoryBoxService.getOne(new LambdaQueryWrapper<MemoryBox>()
                    .eq(MemoryBox::getRootId, box.getId())
                    .eq(MemoryBox::getBoxName,time)
                    .eq(MemoryBox::getUserId,user.getId()));


            if(Objects.isNull(box1)){

                memoryBox.setBoxName(time);
                memoryBox.setRootId(box.getId());
                memoryBox.setUserId(user.getId());
                //创建一级盒子
                memoryBoxService.save(memoryBox);

                //            再查询出该一级盒子的id
                MemoryBox one = memoryBoxService.getOne(new LambdaQueryWrapper<MemoryBox>()
                        .eq(MemoryBox::getRootId, box.getId())
                        .eq(MemoryBox::getBoxName, time));

//          绑定所创建的记忆卡与该盒子的关系，设置创建时间
                card.setBelongToBoxId(one.getId());
                card.setBelongToBoxName(time);


            }else{

                card.setBelongToBoxName(box1.getBoxName());
                card.setBelongToBoxId(box1.getId());

            }


        }else{
//          如果该卡片的根盒子不是卡片盒，即卡片盒的二级或者三级文件夹
            card.setBelongToBoxId(dto.getBelongToBoxId());
            card.setBelongToBoxName(dto.getBelongToBoxName());

        }

//        boolean issuccess = save(card);

        //新增公共卡片记录
        PublicCard publicCard = BeanCopyUtils.copyBean(card, PublicCard.class);
        publicCard.setType("选项卡");
        boolean issuccess =publicCardService.save(publicCard);

        if(!issuccess){
            return AjaxResult.error();
        }
        return AjaxResult.success();

    }
}

