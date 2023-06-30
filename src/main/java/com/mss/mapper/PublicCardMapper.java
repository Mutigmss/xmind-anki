package com.mss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mss.domain.entity.PublicCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * 公共模板卡表，所有卡都通用的公共卡(PublicCard)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-23 12:16:55
 */
@Mapper
public interface PublicCardMapper extends BaseMapper<PublicCard> {


    //查询所有卡片记录
    @Select("select  * from public_card where del_flag=0 and user_id =#{id}")
    public List<PublicCard> selectPublicCards(Long id);

    public List<PublicCard> searchByKeyWord(String keyword);

    @Select("select * from public_card where del_flag=0")
    public List<PublicCard> searchCard();

    public List<PublicCard> searchCardByUserId(@Param("id") Long id);

    @Select("select * from public_card where del_flag=0 and id=#{id}")
    PublicCard selectCardById(Long id);


    @Update("update public_card set learn_count=learn_count-1 where id=#{id}")
    void updateLearnCount(Long id);

    @Update("update public_card set learn_count=learn_count+1 where id=#{id}")
    void addLearnCount(Long id);


}
