package com.mss.utils;

import com.mss.domain.base.Card;
import com.mss.domain.entity.MemoryBox;
import com.mss.domain.entity.MemoryCard;

import java.util.ArrayList;
import java.util.List;

public class CardBoxUtils {

    /**
     * 使用递归方法建子卡片盒，并且查找出盒子的所有卡片
     * @param MemoryBoxList
     * @return
     */
//    public static List<MemoryBox> buildTree(List<MemoryBox> MemoryBoxList,
//                                            List<Card> memoryCardList) {
//
//        List<MemoryBox> trees = new ArrayList<>();
//        for (MemoryBox box : MemoryBoxList) {
//
//            if (box.getRootId() == 0) {
//                trees.add(findChildren(box,MemoryBoxList,memoryCardList));
//            }
//
//        }
//        return trees;
//    }
    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
//    public static MemoryBox findChildren(MemoryBox box, List<MemoryBox> treeNodes,
//                                         List<Card> memoryCardList) {
//
//        // 数据初始化
//        box.setChildrenBox(new ArrayList<MemoryBox>());
//
////        给该盒子的卡片集合添加卡片
//        for (Card card:memoryCardList) {
//
//            box.setCardList(new ArrayList<MemoryCard>());
//
//            if(box.getId().longValue()==card.getBelongToBoxId()){
//                box.getCardList().add(card);
//            }
//
//        }
//
////        遍历递归查找盒子的子盒子
//        for (MemoryBox it : treeNodes) {
//            if(box.getId().longValue() == it.getRootId().longValue()) {
//                if (box.getChildrenBox() == null) {
//                    box.setChildrenBox(new ArrayList<>());
//                }
//                box.getChildrenBox().add(findChildren(it,treeNodes,memoryCardList));
//            }
//        }
//        return box;
//    }

}
