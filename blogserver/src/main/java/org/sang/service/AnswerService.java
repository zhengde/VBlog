package org.sang.service;

import org.sang.bean.Answer;
import org.sang.mapper.AnswerMapper;
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

    public void praise(Long id) {
        answerMapper.praise(id);
    }

    public int answer(Long id, String content) {
        if (content == null || content.length() < 1) {
            return 0;
        }
        answerMapper.answer(id, content);
        return 1;
    }

    public List<Answer> getAnswerByUid(String uid) {
        return answerMapper.getAnswerByUid(uid);
    }
}
