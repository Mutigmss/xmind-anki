package com.mss.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mss.constant.TokenConstant;
import com.mss.domain.dto.BoxLearnSetDto;
import com.mss.domain.entity.*;
import com.mss.domain.po.FillInBackCardPo;
import com.mss.domain.vo.DynamicsCommentVo2;
import com.mss.domain.vo.OptionCardVo;
import com.mss.entity.UserLearn;
import com.mss.mapper.PublicCardMapper;
import com.mss.service.MemoryBoxService;
import com.mss.service.PublicCardService;
import com.mss.service.UserLearnService;
import com.mss.utils.*;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公共模板卡表，所有卡都通用的公共卡(PublicCard)表服务实现类
 *
 * @author makejava
 * @since 2023-03-23 12:16:55
 */
@Service("publicCardService")
public class PublicCardServiceImpl extends ServiceImpl<PublicCardMapper, PublicCard> implements PublicCardService {


    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PublicCardMapper publicCardMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private MemoryBoxService memoryBoxService;

    @Autowired
    private UserLearnService userLearnService;





    /**
     * 获取用户所有卡片，卡片排序按照后先顺序排序
     * @param request
     * @return
     */
    @Override
    public AjaxResult getAllCardByUserId(HttpServletRequest request) {


        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);

//        List<PublicCard> list1 = publicCardService.list(
//                new LambdaQueryWrapper<PublicCard>().eq(PublicCard::getUserId, user.getId()));



        List<PublicCard> cardlist= publicCardMapper.selectPublicCards(user.getId());




        //按照时间后先顺序排序
        cardlist=cardlist.stream()
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


//        再转换为对应的真正实体类
        List<Object> objects = toTargetObject(cardlist);

        return AjaxResult.success(objects);



    }


    /**
     * 获取用户发布的最新20张卡牌
     * @param request
     * @return
     */
    @Override
    public AjaxResult previewCardByUserId(HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        SysUser user = LoginUserUtils.getLoginUser(request, redisCache);



        List<PublicCard> cardlist= publicCardMapper.selectPublicCards(user.getId());


        List<PublicCard> cardList=new ArrayList<PublicCard>();

//        先获取最新的10张卡片
        for (int i = 0; i < 10; i++) {
            cardList.add(cardlist.get(i));
        }

        //按照时间后先顺序排序
        cardList=cardlist.stream()
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


//        再转换为对应的真正实体类
        List<Object> objects = toTargetObject(cardList);

        return AjaxResult.success(objects);


    }


    /**
     * 根据盒子id获取该盒子下的所有卡牌
     * @param request
     * @return
     */
    @Override
    public AjaxResult getAllCardInBox(Long boxId,HttpServletRequest request) {

        SysUser user = LoginUserUtils.getLoginUser(request, redisCache);
        LambdaQueryWrapper<MemoryBox> qw = new LambdaQueryWrapper<>();

        LambdaQueryWrapper<PublicCard> qw1 = new LambdaQueryWrapper<>();

        List<MemoryBox> list= memoryBoxService.list(qw.
                eq(MemoryBox::getUserId, user.getId())
                .eq(MemoryBox::getDelFlag,0));

        List<PublicCard> list1 = publicCardMapper.selectPublicCards(user.getId());

        List<MemoryBox> boxes = MemoryBoxUtils.buildBoxTree(boxId, list, list1);

        MemoryBox box = boxes.get(0);

        List<Object> boxTree = MemoryBoxUtils.findCard(box);

        return AjaxResult.success(boxTree);

    }


    /**
     * 根据卡牌id停学卡牌
     * @param id 卡牌id
     * @return
     */
    @Override
    public AjaxResult stopLearnCardById(Long id) {

//       卡牌停学，学习次数改为-1
        boolean isSuccess = update().setSql("is_stop_learn = 1").eq("id", id).update();


        if(isSuccess){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }

    }

    /**
     * 根据卡牌id重置卡牌
     * @param id
     * @return
     */
    @Override
    public AjaxResult resetCardById(Long id) {

//        重置卡牌，把卡牌变成未学卡牌
        boolean isSuccess = update().setSql("learn_count = 0")
                .eq("id", id).update();

        if(isSuccess){
            return AjaxResult.success();
        }else{
            return AjaxResult.error();
        }
    }


    /**
     * 根据卡片id删除卡片
     * @param id
     * @return
     */
    @Override
    public AjaxResult deleteCardById(Long id) {


//      根据id删除相应的卡片
        removeById(id);

        return AjaxResult.success();

    }

    /**
     * 获取按照学习设置返回的学习卡片数组
     * @param dto
     * @param id
     * @param request
     * @return
     */
    @Override
    public AjaxResult getLearnCardListBySetting(BoxLearnSetDto dto, Long id, HttpServletRequest request) {

        //解析请求头,获取redis中存储的当前登录用户的信息
        String token = request.getHeader("token");
        SysUser user = (SysUser)redisCache.getCacheObject(TokenConstant.TOKEN_KEY + token);


//      查询出属于该盒子的所有卡
        List<PublicCard> list = list(
                new QueryWrapper<PublicCard>()
                        .eq("belong_to_box_id", id)
                        .eq("user_id", user.getId()));


//      再根据卡片盒子设置筛选出所要展示的具体卡片数组


//        boolean b = saveOrUpdateBatch(list);


//       根据卡片盒学习设置，返回相应的卡片集合给前端
        return null;
    }




    /**
     * 根据关键词搜索卡片,返回卡片数组结果,elasticsearch版本
     * @param keyword 关键词
     * @param request
     * @return
     */
    @Override
    public AjaxResult searchCardListByKeyWord(String keyword,
                                              HttpServletRequest request) {

        SysUser user = LoginUserUtils.getLoginUser(request, redisCache);
//      查询elasticsearch中的索引表

        // 拿到要查询的索引
        SearchRequest searchRequest = new SearchRequest("card");

        // 构建查询条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

//        查询条件
         SearchSourceBuilder total =
                 sourceBuilder.query(QueryBuilders.matchQuery("all", keyword));

         searchRequest.source(total);

        List<PublicCard> list = new ArrayList<>();

//      返回结果
        try {

            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();

            for (SearchHit hit : hits) {

                String jsonStr = JSONUtil.toJsonStr(hit.getSourceAsString());

                PublicCard publicCard = JSONUtil.toBean(jsonStr, PublicCard.class);
                publicCard.setId(Long.valueOf(hit.getId()));
                list.add(publicCard);

            }

            System.out.println(user);
            System.out.println(user.getId());
            list= list.stream()
                    .filter(a->a.getUserId()==user.getId())
                    .filter(b->b.getDelFlag().equals(0))
                    .collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Object> objectList = toTargetObject(list);
        return AjaxResult.success(objectList);

    }


    /**
     * 搜索功能，mysql版本
     * @param keyword
     * @param request
     * @return
     */
    @Override
    public AjaxResult searchCardListByKeyWordByMysql(String keyword,
                                                     HttpServletRequest request) {

//        获取登录用户
        SysUser user = LoginUserUtils.getLoginUser(request, redisCache);

        System.out.println(Objects.isNull(user));

        List<PublicCard> list= publicCardMapper.searchByKeyWord(keyword);

        System.out.println(list);
        System.out.println(user.getId().longValue());
        try{
            list=list.stream().filter(b->b.getUserId().longValue()==user.getId().longValue())
                    .collect(Collectors.toList());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        List<Object> objects = toTargetObject(list);
        return AjaxResult.success(objects);

    }


    /**
     * 学习或者复习一张卡片
     * @param id
     * @return
     */
    @Override
    public AjaxResult learnCard(Long id,String learnStatus) {


//        先查询出该卡牌的学习次数
//        PublicCard card = getById(id);

        PublicCard card =publicCardMapper.selectCardById(id);
        if(learnStatus.equals("忘记")){

            if(card.getLearnCount()>0){

                publicCardMapper.updateLearnCount(id);

                UserLearn userLearn = new UserLearn();
                userLearn.setCardId(card.getId());
                userLearn.setUserId(card.getUserId());
                userLearnService.save(userLearn);
            }

//            if(card.getLearnCount()>0)
//            update().setSql("learn_count=learn_count-1").eq("id",id).update();



        }else if(learnStatus.equals("模糊")){

            UserLearn userLearn = new UserLearn();
            userLearn.setCardId(card.getId());
            userLearn.setUserId(card.getUserId());
            userLearnService.save(userLearn);

        }else if(learnStatus.equals("掌握")){

            if(card.getLearnCount()<6){

                publicCardMapper.addLearnCount(id);

                UserLearn userLearn = new UserLearn();
                userLearn.setCardId(card.getId());
                userLearn.setUserId(card.getUserId());
                userLearnService.save(userLearn);

            }

//            if(card.getLearnCount()<6)
//            update().setSql("learn_count=learn_count+1").eq("id",id).update();

        }

        return AjaxResult.success();

    }


    /**
     * 获取根据id卡片详情
     * @param id
     * @return
     */
    @Override
    public PublicCard getByCardById(Long id) {

        PublicCard byId = getById(id);
        return byId;

    }



    /**
     * 获取用户待学习与待复习的所有卡片id
     * @param request
     * @return
     */
    @Override
    public AjaxResult toLearn(HttpServletRequest request) {

        SysUser user = LoginUserUtils.getLoginUser(request,redisCache);
//        找出所有该用户的卡片盒子
        List<MemoryBox> list = memoryBoxService.list(new QueryWrapper<MemoryBox>().eq("del_flag", 0)
                .eq("user_id", user.getId()).eq("is_stop_learn", 0));

        List<Object>learnCard=new ArrayList<>();
        List<PublicCard>toLearnCard=new ArrayList<>();

//        再根据卡片盒子学习偏好返回相应的卡片数量
        for (MemoryBox box:list) {

//          找出该盒子下的所有卡片
            List<Object> card = MemoryBoxUtils.findCard(box);

//            先转成publicCard对象数组
            List<PublicCard> cards=null;

            try {
               cards = MemoryBoxUtils.returnPublicCardList(card);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            //    卡牌盒子学习场景为未设置或者长期记忆
            if(box.getLearnCondition()==3||box.getLearnCondition()==1){

                List<PublicCard> collect = cards.stream()
                        .filter(a -> a.getLearnCount() == 0)
                        .collect(Collectors.toList());

                if(collect.size()==box.getEverydayNewCardLimit()){

                    learnCard.addAll(MemoryBoxUtils.toTargetObject(collect));

                }else if(collect.size()==0){

                    //按照学习次数降序排序,获取新卡张数的卡牌，虽然都不是新卡，但是卡牌数量要一样
                    List<PublicCard> collect1 = cards.stream()
                            .sorted(Comparator.comparing(PublicCard::getLearnCount).reversed())
                            .limit(box.getEverydayNewCardLimit())
                            .collect(Collectors.toList());


                    learnCard.addAll(MemoryBoxUtils.toTargetObject(collect1));

                }else if(collect.size()<box.getEverydayNewCardLimit()){

                    learnCard.addAll(MemoryBoxUtils.toTargetObject(collect));
                    //按照学习次数降序排序,获取剩余卡牌，新卡数量不足，获取一下学习次数少的卡牌填充至，卡牌盒规定的学习数量
                    List<PublicCard> collect2 = cards.stream()
                            .sorted(Comparator.comparing(PublicCard::getLearnCount).reversed())
                            .limit(box.getEverydayNewCardLimit()-collect.size())
                            .collect(Collectors.toList());
                    learnCard.addAll(MemoryBoxUtils.toTargetObject(collect2));

                }


            }else if(box.getLearnCondition()==2){

                //  卡牌盒子学习场景为复习考试
                long time = box.getExamTime().getTime();
                long time1 = new Date().getTime();
                SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = dateFormat.format(time);
                String date1 = dateFormat.format(time1);

                int days=new Integer(date).intValue()-new Integer(date1).intValue();

                int num = cards.size() / days;

                List<PublicCard> cardList = cards.stream()
                        .limit(num)
                        .collect(Collectors.toList());

                learnCard.add(MemoryBoxUtils.toTargetObject(cardList));

            }


        }


        List<UserLearn> userLearns = userLearnService.list();

//        b.getCreateTime().getTime()
//        new Date().getTime()
        List<UserLearn> userLearnList = userLearns.stream()
                .filter(b -> new SimpleDateFormat("yyyy-MM-dd").format( b.getCreateTime().getTime())
                        ==new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()) )
                .collect(Collectors.toList());


        return AjaxResult.success(learnCard).put("alearn",userLearnList.size());

    }



    /**
     * 返回相应实体类
     * @param list
     * @return
     */
    public List<Object> toTargetObject( List<PublicCard> list){

        List<Object> lists = new ArrayList<>();
        for (PublicCard card:list) {

            System.out.println(card.getType());
            if(card.getType().equals("记忆卡")){

                MemoryCard memoryCard = BeanCopyUtils.copyBean(card, MemoryCard.class);
                lists.add(memoryCard);

            }else if(card.getType().equals("笔记卡")){

              NoteCard noteCard = BeanCopyUtils.copyBean(card, NoteCard.class);
              lists.add(noteCard);

            }else if(card.getType().equals("智能单词卡")){

                WordCard wordCard = BeanCopyUtils.copyBean(card, WordCard.class);
                lists.add(wordCard);

            }else if(card.getType().equals("选项卡")){

                String[] strings = card.getOption().split("\n");

                OptionCardVo vo = BeanCopyUtils.copyBean(card, OptionCardVo.class);
                ArrayList<String> strings1 = new ArrayList<>();
                for (String s:strings) {

                    strings1.add(s);

                }
                vo.setOptionAarry(strings1);
                lists.add(vo);

            }else if(card.getType().equals("填空卡")){

                FillInBackCard wordCard = BeanCopyUtils.copyBean(card, FillInBackCard.class);
                lists.add(wordCard);

            }

        }

        return lists;
    }


    /**
     * 根据盒子id获取该盒子下的所有卡牌
     * @param boxId
     * @param list
     * @return
     */
    public  List<PublicCard> getAllCardInBox(Long boxId,List<PublicCard>list){





        return null;
    }

}

