package com.mss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.ChildMemoryBoxDto;
import com.mss.domain.dto.MemoryBoxDto;
import com.mss.domain.dto.UpdateMemoryBoxDto;
import com.mss.domain.entity.*;
import com.mss.domain.po.MemoryBoxPo;
import com.mss.domain.vo.MemoryBoxDetailsVo;
import com.mss.enums.AppHttpCodeEnum;
import com.mss.exception.SystemException;
import com.mss.mapper.MemoryBoxMapper;
import com.mss.mapper.PublicCardMapper;
import com.mss.service.MemoryBoxService;
import com.mss.service.MemoryCardService;
import com.mss.service.OssService;
import com.mss.service.PublicCardService;
import com.mss.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mss.service.impl.OssServiceImpl.getcontentType;

/**
 * 用户闪记盒(MemoryBox)表服务实现类
 *
 * @author makejava
 * @since 2023-03-18 14:48:27
 */
@Service("memoryBoxService")
public class MemoryBoxServiceImpl extends ServiceImpl<MemoryBoxMapper, MemoryBox> implements MemoryBoxService {

    @Autowired
    private RedisCache redisCache;

//    @Autowired
//    private MemoryCardService memoryCardService;

    @Autowired
    private MemoryBoxService memoryBoxService;

    @Autowired
    private PublicCardService publicCardService;

    @Autowired
    private PublicCardMapper publicCardMapper;

    @Autowired
    private OssService orangeService;





    /**
     * 创建卡牌盒子
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult createCardBox(MemoryBoxDto dto, HttpServletRequest request) {

        SysUser user = LoginUserUtils.getLoginUser(request,redisCache);


        MemoryBox memoryBox = BeanCopyUtils.copyBean(dto, MemoryBox.class);
        memoryBox.setUserId(user.getId());
        //所有创建的卡牌盒都是根盒子
        memoryBox.setRootId(0L);

        boolean issuccess = save(memoryBox);

        if(!issuccess){
            return AjaxResult.error();
        }
        return AjaxResult.success();

    }



    /**
     * 获取卡牌盒子树
     * @param request
     * @return
     */
    @Override
    public AjaxResult getUserMemoryBoxTree(HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        final SysUser user = LoginUserUtils.getLoginUser(request, redisCache);
        //查询所有未删除的卡片盒子
        LambdaQueryWrapper<MemoryBox> qw = new LambdaQueryWrapper<>();
        qw.eq(MemoryBox::getUserId,user.getId())
                .eq(MemoryBox::getDelFlag,0);
        List<MemoryBox> boxList = list(qw);

        LambdaQueryWrapper<PublicCard> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(PublicCard::getUserId, user.getId());

        List<PublicCard> cardList = publicCardMapper.searchCardByUserId(user.getId());

        List<MemoryBox> memoryBoxes = MemoryBoxUtils.buildTree(boxList, cardList);

        List<MemoryBoxPo> boxPos = BeanCopyUtils.copyBeanList(memoryBoxes, MemoryBoxPo.class);

        return AjaxResult.success(boxPos);

    }

    /**
     * 根据id停学卡牌盒
     * @param id
     * @return
     */
    @Override
    public AjaxResult stopLearnBoxById(Long id) {

        //停学卡牌盒
        boolean isSuccess = update().setSql("is_stop_learn = 1").eq("id", id).update();
        if(isSuccess){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }

    }

    /**
     * 根据id重置卡牌盒
     * @param id
     * @return
     */
    @Override
    public AjaxResult resetBoxById(Long id,HttpServletRequest request) {


//      先获取该盒子下的所有卡牌
        SysUser user = LoginUserUtils.getLoginUser(request, redisCache);
        LambdaQueryWrapper<MemoryBox> qw = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<PublicCard> qw1 = new LambdaQueryWrapper<>();

        List<MemoryBox> list= memoryBoxService.list(qw.
                eq(MemoryBox::getUserId, user.getId())
                .eq(MemoryBox::getDelFlag,0));

        List<PublicCard> list1 = publicCardMapper.selectPublicCards(user.getId());

        List<MemoryBox> boxes = MemoryBoxUtils.buildBoxTree(id, list, list1);

        MemoryBox box = boxes.get(0);

        List<Object> allCard = MemoryBoxUtils.findCard(box);

//      查询到所有卡牌
        List<PublicCard> publicCards=null;
        try {
            publicCards = MemoryBoxUtils.returnPublicCardList(allCard);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

//       重置卡牌，学习次数变为0
        for (PublicCard a:publicCards) {
            a.setLearnCount(0);
        }


//        批量修改
        boolean issucess = publicCardService.updateBatchById(publicCards, 1000);

        //停学卡牌盒
        boolean isSuccess = update().setSql("is_stop_learn = 1").eq("id", id).update();

        if(isSuccess){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }

    }

    /**
     * 修改卡片盒子
     * @param dto
     * @param file
     * @return
     */
    @Override
    public AjaxResult updateBox(UpdateMemoryBoxDto dto, MultipartFile file) {

//        上传新的盒子图片
        String s = orangeService.uploadbaseFile(file);

//        修改数据库
        boolean issuccess = update()
                .setSql("box_name=" + dto.getBoxName())
                .setSql("cover_photo="+s)
                .eq("id", dto.getId()).update();

//        返回编辑好的数据给前端
        return AjaxResult.success().put("boxName",dto.getBoxName()).put("cover_photo",s);


    }

    /**
     * 用户创建根卡片盒
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult createMemoryBox(MemoryBoxDto dto, MultipartFile file, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        MemoryBox memoryBox = BeanCopyUtils.copyBean(dto, MemoryBox.class);
        memoryBox.setUserId(user.getId());

//      用户创建根卡片盒
        memoryBox.setRootId(0L);

        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
        String accessKeyId = "LTAI5tJpgfTDg94vqwb5PfyA";
        String accessKeySecret = "cyvhqPFopaiENjXOBcvPqRM38KiM5l";
        String bucketName = "test-anki";

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
        String url = result.getResponse().getUri();

        // 关闭OSSClient。
        ossClient.shutdown();
//        设置卡片盒子封面照片
        memoryBox.setCoverPhoto(url);


        boolean issuccess = save(memoryBox);

        if(!issuccess){
            return AjaxResult.error();
        }
        return AjaxResult.success();

    }

    /**
     * 获取用户卡片盒子列表
     * @param request
     * @return
     */
    @Override
    public AjaxResult getUserMemoryBoxList(HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        System.out.println(user);
        //查询所有未删除的卡片盒子
        LambdaQueryWrapper<MemoryBox> qw = new LambdaQueryWrapper<>();
        qw.eq(MemoryBox::getUserId,user.getId())
                .eq(MemoryBox::getDelFlag,0);
        List<MemoryBox> boxList = list(qw);

        LambdaQueryWrapper<PublicCard> qw1 = new LambdaQueryWrapper<>();
        qw1.eq(PublicCard::getUserId, user.getId());

//        List<PublicCard> cardList = publicCardService.list(qw1);

//        按照后先顺序排序
        boxList=boxList.stream()
                .sorted(new Comparator<MemoryBox>() {
                            @Override
                            public int compare(MemoryBox o1,MemoryBox o2) {

                                if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                                {
                                    return 1;

                                }else if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                                {
                                    return -1;
                                }else{
                                    return 0;
                                }
                            }

                        }
                ).collect(Collectors.toList());

        List<PublicCard> cardList = publicCardMapper.searchCardByUserId(user.getId());

        cardList=cardList.stream()
                .sorted(new Comparator<PublicCard>() {
                            @Override
                            public int compare(PublicCard o1,PublicCard o2) {

                                if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                                {
                                    return 1;

                                }else if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                                {
                                    return -1;
                                }else{
                                    return 0;
                                }
                            }

                        }
        ).collect(Collectors.toList());


        List<MemoryBox> memoryBoxes = MemoryBoxUtils.buildTree(boxList, cardList);


        for (MemoryBox memoryBox : memoryBoxes) {

            //查找该根卡牌盒子下的所有卡牌
            List<Object> card = MemoryBoxUtils.findCard(memoryBox);

            List<PublicCard> cards=null;

            int total=0;

            try {

                cards= MemoryBoxUtils.returnPublicCardList(card);
                total=cardList.size();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            if(Objects.isNull(cards)){
                throw new SystemException(AppHttpCodeEnum.NULL_Pointer_Exception);
            }

            for (PublicCard c:cards) {
                System.out.println(c.toString());
            }


//        未学卡牌数组
            List<PublicCard>noStudyList=new ArrayList<PublicCard>();
            noStudyList=cards.stream().filter(c -> c.getLearnCount()==0)
                    .collect(Collectors.toList());

//        记忆中卡牌数组
            List<PublicCard>memoryList=new ArrayList<PublicCard>();
            memoryList=cards.stream()
                    .filter(c -> c.getLearnCount()>0&&c.getLearnCount()<6)
                    .collect(Collectors.toList());

//        已掌握卡牌数组
            List<PublicCard>masterList=new ArrayList<PublicCard>();
            masterList=cards.stream()
                    .filter(c -> c.getLearnCount()==6)
                    .collect(Collectors.toList());

//        停止学习卡牌数组
            List<PublicCard>stopLearnList=new ArrayList<PublicCard>();
            stopLearnList=cards.stream()
                    .filter(c -> c.getIsStopLearn()==1)
                    .collect(Collectors.toList());


            memoryBox.setStopCardNumber(stopLearnList.size());
            memoryBox.setNoStudyCardNumber(noStudyList.size());
            System.out.println(noStudyList.size());
            memoryBox.setMasterCardNumber(masterList.size());
            memoryBox.setMemoryCardNumber(memoryList.size());


        }



        return AjaxResult.success(memoryBoxes);

    }

    /**
     * 在卡片盒子创建文件夹（子盒子）
     * @param dto
     * @param request
     * @return
     */
    @Override
    public AjaxResult createChildBox(ChildMemoryBoxDto dto, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

        MemoryBox box = BeanCopyUtils.copyBean(dto, MemoryBox.class);
        box.setRootId(dto.getFatherBoxId());
        box.setUserId(user.getId());

        boolean issuccess = save(box);

        if(!issuccess){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return AjaxResult.success("创建文件夹成功！");
    }



    /**
     * 根据卡片盒子id查询卡片盒子详情
     * @param id
     * @return
     */
    @Override
    public AjaxResult getBoxDetailsByBoxId(Long id) {


        MemoryBox box = getById(id);

        List<MemoryBox> list = memoryBoxService.list(
                new LambdaQueryWrapper<MemoryBox>().eq(MemoryBox::getDelFlag,0));

        List<PublicCard> cardList= publicCardMapper.searchCard();

//        卡片排序，最新发布的在最前面
        cardList=cardList.stream().sorted(new Comparator<PublicCard>() {
            @Override
            public int compare(PublicCard o1,PublicCard o2) {

//               动态按照最新发布顺序来排序，最新发布，就排在最前面
                if(o1.getCreateTime().getTime()<o2.getCreateTime().getTime())
                {
                    return 1;
                }else if(o1.getCreateTime().getTime()>o2.getCreateTime().getTime())
                {
                    return -1;
                }else{
                    return 0;
                }
            }
        }).collect(Collectors.toList());

//        递归构造卡片盒子树
        MemoryBox memoryBox = MemoryBoxUtils.findChildren(box, list, cardList);

//        查找该根卡牌盒子下的所有卡牌
        List<Object> card = MemoryBoxUtils.findCard(memoryBox);

        List<PublicCard> cards=null;

        int total=0;

        try {


            cards= MemoryBoxUtils.returnPublicCardList(card);
            total=cardList.size();


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


//        System.out.println(cards.size());

        if(Objects.isNull(cards)){
            throw new SystemException(AppHttpCodeEnum.NULL_Pointer_Exception);
        }

        for (PublicCard c:cards) {
            System.out.println(c.toString());
        }


//        cards = cards.stream()
//                .filter(a -> a.getIsStopLearn().equals(0))
//                .collect(Collectors.toList());
//
//        int stopLearnCount=total-cards.size();
//        int unStudyCount=
//                cards.stream()
//                        .filter(a -> a.getLearnCount()==0)
//                        .collect(Collectors.toList()).size();
//
//        int memoryCount=cards.stream()
//                .filter(a -> a.getLearnCount()>=1&&a.getLearnCount()<6)
//                .collect(Collectors.toList()).size();
//
//        int MasteredCount=cards.stream()
//                .filter(a -> a.getLearnCount()==6)
//                .collect(Collectors.toList()).size();
//
//        vo.setRememberCardNumber(memoryCount);
//        vo.setStopCardNumber(stopLearnCount);
//        vo.setAlreadyMasteredCardNumber(MasteredCount);
//        vo.setTotalCardNumber(cards.size());
//        vo.setUnStudyCardNumber(unStudyCount);
//        return AjaxResult.success(vo);


//        未学卡牌数组
        List<PublicCard>noStudyList=new ArrayList<PublicCard>();
        noStudyList=cards.stream().filter(c -> c.getLearnCount()==0).collect(Collectors.toList());

//        记忆中卡牌数组
        List<PublicCard>memoryList=new ArrayList<PublicCard>();
                        memoryList=cards.stream()
                                        .filter(c -> c.getLearnCount()>0&&c.getLearnCount()<6)
                                        .collect(Collectors.toList());

//        已掌握卡牌数组
        List<PublicCard>masterList=new ArrayList<PublicCard>();
                        masterList=cards.stream()
                                .filter(c -> c.getLearnCount()==6)
                                .collect(Collectors.toList());

//        停止学习卡牌数组
        List<PublicCard>stopLearnList=new ArrayList<PublicCard>();
                        stopLearnList=cards.stream()
                                              .filter(c -> c.getIsStopLearn()==1)
                                              .collect(Collectors.toList());

        MemoryBoxDetailsVo vo = BeanCopyUtils.copyBean(memoryBox, MemoryBoxDetailsVo.class);

//        全部卡牌
//        vo.setAllCardList(card);

//        vo.setNoStudyCardList(MemoryBoxUtils.toTargetObject(noStudyList));
//
//        vo.setMemoryCardList(MemoryBoxUtils.toTargetObject(memoryList));
//
//        vo.setStopLearnCardList(MemoryBoxUtils.toTargetObject(stopLearnList));




        return AjaxResult.success()
                .put("box",vo)
                .put("allCardList",card)
                .put("noStudyList",noStudyList)
                .put("memoryList",memoryList)
                .put("masterList",masterList)
                .put("stopLearnCardList",stopLearnList);


    }


}

