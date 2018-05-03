package org.sang.service;

import org.sang.bean.Answer;
import org.sang.bean.Comment;
import org.sang.mapper.AnswerMapper;
import org.sang.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AnswerService {

    @Autowired
    AnswerMapper answerMapper;

    public List<Answer> getAnswerByAid(Long aid) {
        return answerMapper.getAnswerByAid(aid);
    }

    public void praise(Long id){
        answerMapper.praise(id);
    }

    public void answer(Long id, String content) {

    }
}
