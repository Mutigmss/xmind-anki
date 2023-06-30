package com.mss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.NoteCardDto;
import com.mss.domain.entity.*;
import com.mss.mapper.NoteCardMapper;
import com.mss.service.MemoryBoxService;
import com.mss.service.NoteCardService;
import com.mss.service.PublicCardService;
import com.mss.utils.AjaxResult;
import com.mss.utils.BeanCopyUtils;
import com.mss.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 用户笔记卡表(NoteCard)表服务实现类
 *
 * @author makejava
 * @since 2023-03-21 17:22:38
 */
@Service("noteCardService")
public class NoteCardServiceImpl extends ServiceImpl<NoteCardMapper, NoteCard> implements NoteCardService {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MemoryBoxService memoryBoxService;

    @Autowired
    private PublicCardService publicCardService;

    /**
     * 创建用户笔记卡
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult createNoteCard(NoteCardDto dto, HttpServletRequest request) {


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        NoteCard card = BeanCopyUtils.copyBean(dto, NoteCard.class);
        card.setUserId(user.getId());


        MemoryBox box = memoryBoxService.getOne( new LambdaQueryWrapper<MemoryBox>()
                .eq(MemoryBox::getId, dto.getBelongToBoxId()));
//                .eq(MemoryBox::getBoxName,dto.getBelongToBoxName()));

        //若
        if(box.getRootId().longValue()==0){

            MemoryBox memoryBox = new MemoryBox();
            memoryBox.setRootId(box.getId());
            Date ss = new Date();


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

            card.setBelongToBoxId(dto.getBelongToBoxId());
            card.setBelongToBoxName(dto.getBelongToBoxName());

        }



        //          新增公共卡片记录
        PublicCard publicCard = BeanCopyUtils.copyBean(card, PublicCard.class);
        publicCard.setType("笔记卡");
        boolean issuccess =publicCardService.save(publicCard);

        if(!issuccess){
            return AjaxResult.error();
        }
        return AjaxResult.success();

    }
}

