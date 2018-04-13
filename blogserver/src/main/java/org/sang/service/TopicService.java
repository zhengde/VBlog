package org.sang.service;

import org.sang.bean.Topic;
import org.sang.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicMapper topicMapper;

    public List<Topic> getAllTopic(){
        return topicMapper.getAllTopic();
    }

}
