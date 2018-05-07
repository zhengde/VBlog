package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sang.bean.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper {

    List<Answer> getAnswerByAid(Long aid);

    void praise(Long id);

    int answer(@Param("id")Long id,@Param("content") String content);

    List<Answer> getAnswerByUid(String uid);

    void cancelPraise(Long id);
}
