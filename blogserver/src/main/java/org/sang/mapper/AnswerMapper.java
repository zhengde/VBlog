package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sang.bean.Answer;

import java.util.List;

@Mapper
public interface AnswerMapper {

    List<Answer> getAnswerByAid(Long aid);

    void praise(Long id);
}
