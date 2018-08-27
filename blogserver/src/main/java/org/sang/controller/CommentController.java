package org.sang.controller;

import org.sang.bean.Answer;
import org.sang.bean.Comment;
import org.sang.bean.RespBean;
import org.sang.service.AnswerService;
import org.sang.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * @param id      回答id
     * @param uid     用户id
     * @param content 评论内容
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public RespBean comment(Long id, String uid, String content, String topId, String parentId) {
        int result = commentService.comment(id, uid, content, topId, parentId);
        if (result == 1) {
            return new RespBean("success", "评论成功");
        } else {
            return new RespBean("error", "评论失败");
        }
    }

    /**
     * 删除评论，仅管理员有权，非管理员也不会在前端出现「删除」按钮
     */
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
    public RespBean remove(@PathVariable Long id) {
        if (commentService.remove(id)) {
            return new RespBean("success", "删除成功!");
        }
        return new RespBean("error", "删除失败!");
    }

    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    public List<Comment> getComment(String answerIds) {
        String[] result = answerIds.split(",");
        return commentService.getComment(result);
    }


}
