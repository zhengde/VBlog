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

    public List<Comment> getCommentByAid(Long aid) {

        return commentMapper.getCommentByAid(aid);
    }
}
