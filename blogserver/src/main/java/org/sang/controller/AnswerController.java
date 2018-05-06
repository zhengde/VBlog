package org.sang.controller;

import org.sang.bean.Answer;
import org.sang.bean.RespBean;
import org.sang.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public RespBean answer(@PathVariable Long id, String content) {
        int result = answerService.answer(id, content);
        if (result == 1) {
            return new RespBean("success", "回答成功");
        } else {
            return new RespBean("error", "回答失败");
        }
    }

    /**
     * 根据用户 id，查询该用户回答问题的数据
     */
    @RequestMapping(value = "/getAnswerByUid", method = RequestMethod.GET)
    public List<Answer> getAnswerByUid(@RequestParam String uid) {
        List<Answer> answerArticles = answerService.getAnswerByUid(uid);
        return answerArticles;
    }
}
