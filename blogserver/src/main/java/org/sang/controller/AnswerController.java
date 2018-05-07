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

    @RequestMapping(value = "/cancelPraise", method = RequestMethod.POST)
    public void cancelPraise(Long id) {
        answerService.cancelPraise(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean answer(Long id, String uid, String content, String title) {
        int result = answerService.answer(id, uid, content, title);
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

    /**
     * 删除回答，仅管理员有权，非管理员也不会在前端出现「删除」按钮
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
    public RespBean remove(@PathVariable Long id) {
        if (answerService.remove(id)) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }


}
