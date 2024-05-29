package com.example.jhta_3team_finalproject.mybatis.mapper;

import com.example.jhta_3team_finalproject.domain.inquery.InqueryComment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface InqCommentMapper {

    int getListCount(int board_num);

    int commentsInsert(InqueryComment c);

    List<InqueryComment> getCommentList(Map<String, Object> map);

    int commentsDelete(int num);

    int commentsUpdate(InqueryComment co);

    int commentReplyUpdate(InqueryComment co);

    int commentReply(InqueryComment co);
}
