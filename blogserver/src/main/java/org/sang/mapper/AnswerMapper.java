package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sang.bean.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper {

    List<Answer> getAnswerByAid(Long aid);

    void praise(Long id);

    void answer(@Param("aid") Long id,@Param("uid")  String uid, @Param("content") String content, @Param("title") String title,@Param("create_time") String createTime);

    List<Answer> getAnswerByUid(String uid);

    void cancelPraise(Long id);

    boolean remove(Long id);

}
