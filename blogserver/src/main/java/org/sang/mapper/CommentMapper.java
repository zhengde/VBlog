package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.sang.bean.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> getCommentByAid(Long aid);
}
