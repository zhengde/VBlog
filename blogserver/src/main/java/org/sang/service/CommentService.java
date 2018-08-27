package org.sang.service;

import org.sang.bean.Comment;
import org.sang.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    @Autowired
    CommentMapper commentMapper;

    public boolean remove(Long id) {
        return commentMapper.remove(id);
    }

    public int comment(Long id, String uid, String content, String topId, String parentId) {
        if (content == null || content.length() < 1) {
            return 0;
        }
        commentMapper.comment(id, uid, content, String.valueOf(System.currentTimeMillis()), "0", "0");
        return 1;
    }

    public List<Comment> getComment(String[] answerIds) {
        return commentMapper.getComment(answerIds);

    }
}
