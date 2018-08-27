package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sang.bean.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {


    boolean remove(Long id);

    void comment(@Param("id") Long id,@Param("uid") String uid,@Param("content") String content,@Param("create_time") String createTime,@Param("top_id")String top_id,@Param("parent_id") String parent_id);

    List<Comment> getComment(@Param("answerIds") String[] answerIds);
}
