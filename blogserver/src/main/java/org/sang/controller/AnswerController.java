package org.sang.controller;

import org.sang.bean.Topic;
import org.sang.service.AnswerService;
import org.sang.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @RequestMapping(value = "/praise", method = RequestMethod.POST)
    public void praise(Long id) {
        answerService.praise(id);
    }

    @RequestMapping(value = "/{aid}", method = RequestMethod.POST)
    public void answer(@PathVariable Long id, String content) {
        answerService.answer(id, content);
    }

}
