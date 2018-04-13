package org.sang.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.sang.bean.Article;
import org.sang.bean.Topic;

import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
@Mapper
public interface TopicMapper {

    List<Topic> getAllTopic();

}
