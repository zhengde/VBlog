package org.sang.controller;

import org.sang.bean.RespBean;
import org.sang.bean.Topic;
import org.sang.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
public class TopicController {

    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "/topic", method = RequestMethod.GET)
    public List<Topic> getAllTopic() {
        return topicService.getAllTopic();
    }


}
