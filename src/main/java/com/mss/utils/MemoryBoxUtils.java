package com.mss.utils;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.mss.domain.entity.*;
import com.mss.domain.vo.OptionCardVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryBoxUtils {


    /**
     * 查找出卡片盒子中的所有卡片
     * @param box
     * @return
     */
    public static List<Object>findCard(MemoryBox box){

        List<Object> list = new ArrayList<>();
        if(box.getCardList().size()!=0){
            for (Object card : box.getCardList()) {
                list.add(card);
            }
        }

        if(box.getChildrenBox().size()!=0){
            for (MemoryBox childbox:box.getChildrenBox()) {
                list.addAll(findCard(childbox));
            }
        }

        return list;

    }






    /**
     * 使用递归方法建子卡片盒，并且查找出盒子集合中的所有卡片和子盒子，构建树集合
     * @param MemoryBoxList
     * @return
     */
    public static List<MemoryBox> buildTree(List<MemoryBox> MemoryBoxList,
                                            List<PublicCard> memoryCardList) {

        List<MemoryBox> trees = new ArrayList<>();
        for (MemoryBox box : MemoryBoxList) {

            if (box.getRootId() == 0) {
                trees.add(findChildren(box,MemoryBoxList,memoryCardList));
            }

        }
        return trees;
    }

    /**
     * 使用递归方法建子卡片盒，并且查找出单个盒子中的所有卡片和子盒子，构建单个盒子树集合
     * @param BoxId
     * @param MemoryBoxList
     * @param memoryCardList
     * @return
     */
    public static List<MemoryBox> buildBoxTree(Long BoxId,
                                               List<MemoryBox> MemoryBoxList,
                                              List<PublicCard> memoryCardList) {

        List<MemoryBox> trees = new ArrayList<>();
        for (MemoryBox box : MemoryBoxList) {

            if (box.getRootId() == 0&&box.getId()==BoxId) {
                trees.add(findChildren(box,MemoryBoxList,memoryCardList));
            }

        }
        return trees;
    }


    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    public static MemoryBox findChildren(MemoryBox box, List<MemoryBox> treeNodes,
                                         List<PublicCard> memoryCardList) {

        // 数据初始化
        box.setChildrenBox(new ArrayList<MemoryBox>());

//        给该盒子的卡片集合添加卡片
        List<PublicCard> list = new ArrayList<>();
        for (PublicCard card:memoryCardList) {

//            box.setCardList(new ArrayList<Object>());
            if(box.getId().longValue()==card.getBelongToBoxId()){

                list.add(card);
//                box.getCardList().add(card);
            }
        }

        List<Object> objects = toTargetObject(list);
        box.setCardList(objects);

//        遍历递归查找盒子的子盒子
        for (MemoryBox it : treeNodes) {
            if(box.getId().longValue() == it.getRootId().longValue()) {

                if (box.getChildrenBox() == null) {
                   box.setChildrenBox(new ArrayList<MemoryBox>());
                }
                box.getChildrenBox().add(findChildren(it,treeNodes,memoryCardList));
            }
        }
        return box;
    }




    /**
     * 返回相应实体类集合
     * @param list
     * @return
     */
    public static List<Object> toTargetObject( List<PublicCard> list){

        List<Object> lists = new ArrayList<>();
        for (PublicCard card:list) {

            System.out.println(card.getType());
            if(card.getType().equals("记忆卡")){

                MemoryCard memoryCard = BeanCopyUtils.copyBean(card, MemoryCard.class);
                lists.add(memoryCard);

            }else if(card.getType().equals("笔记卡")){

                NoteCard noteCard = BeanCopyUtils.copyBean(card, NoteCard.class);
                lists.add(noteCard);

            }else if(card.getType().equals("单词卡")){

                WordCard wordCard = BeanCopyUtils.copyBean(card, WordCard.class);
                lists.add(wordCard);

            }else if(card.getType().equals("选项卡")){

                //
                String[] strings = card.getOption().split("\n");

                OptionCardVo vo = BeanCopyUtils.copyBean(card, OptionCardVo.class);
                ArrayList<String> strings1 = new ArrayList<>();
                for (String s:strings) {

                    strings1.add(s);

                }

                vo.setOptionAarry(strings1);
                lists.add(vo);

            }else if(card.getType().equals("正反卡")){

               FrontBackCard frontBackCard = BeanCopyUtils.copyBean(card, FrontBackCard.class);
               lists.add(frontBackCard);

            }

        }
        return lists;
    }


    /**
     * 返回卡片的真正类型对象
     * @param card
     * @return
     */
    public static Object toTargetObject(PublicCard card){

        HashMap<String,Object> map = new HashMap<>();

        System.out.println(card.getType());
        if(card.getType().equals("记忆卡")){

                MemoryCard memoryCard = BeanCopyUtils.copyBean(card, MemoryCard.class);
                memoryCard.setType("记忆卡");
                return memoryCard;

            }else if(card.getType().equals("笔记卡")){

                NoteCard noteCard = BeanCopyUtils.copyBean(card, NoteCard.class);
                noteCard.setType("笔记卡");
                return noteCard;

            }else if(card.getType().equals("单词卡")){

                WordCard wordCard = BeanCopyUtils.copyBean(card, WordCard.class);

                wordCard.setType("单词卡");
                return wordCard;

            }else if(card.getType().equals("选项卡")){

                OptionCard vo = BeanCopyUtils.copyBean(card, OptionCard.class);
                vo.setType("选项卡");
                return vo;

            }else if(card.getType().equals("正反卡")){

                FrontBackCard frontBackCard = BeanCopyUtils.copyBean(card, FrontBackCard.class);
                frontBackCard.setType("正反卡");
                return frontBackCard;
            }

        return null;

    }


    /**
     * 返回目标PublicCard集合
     * @param list
     * @return
     */
    public static List<PublicCard> returnPublicCardList(List<Object>list) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        List<PublicCard>list1=new ArrayList<>();

        for (Object o :list) {

            System.out.println(o.toString());

            StringBuilder name = new StringBuilder(o.getClass().getName());

            String[] strings = name.toString().split("com.mss.domain.entity.");

            String classname=null;

            for (String s:strings) {
                classname=s;
            }


            PublicCard newcard=null;

            if(classname.equals("MemoryCard"))
            {

                System.out.println(classname);
               MemoryCard memoryCard = BeanCopyUtils.copyBean(o, MemoryCard.class);
                newcard=BeanCopyUtils.copyBean(memoryCard,PublicCard.class);

            }else if(classname.equals("NoteCard")){
                System.out.println(classname);
                NoteCard noteCard = BeanCopyUtils.copyBean(o, NoteCard.class);
                newcard=BeanCopyUtils.copyBean(noteCard,PublicCard.class);

            }else if(classname.equals("FrontBackCard")){

                System.out.println(classname);
                FrontBackCard frontBackCard = BeanCopyUtils.copyBean(o, FrontBackCard.class);
                newcard=BeanCopyUtils.copyBean(frontBackCard,PublicCard.class);

            }else if(classname.equals("WordCard")){

                System.out.println(classname);
                WordCard wordCard = BeanCopyUtils.copyBean(o, WordCard.class);
                newcard=BeanCopyUtils.copyBean(wordCard,PublicCard.class);

            }else if(classname.equals("OptionCard"))
            {
                System.out.println(classname);
                OptionCard optionCard = BeanCopyUtils.copyBean(o, OptionCard.class);
                newcard=BeanCopyUtils.copyBean(optionCard,PublicCard.class);
            }

            list1.add(newcard);
            System.out.println(newcard.toString());
            System.out.println(name);

        }

        return list1;
    }
}
