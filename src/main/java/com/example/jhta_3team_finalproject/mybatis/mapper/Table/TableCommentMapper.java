package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import com.example.jhta_3team_finalproject.domain.Table.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TableCommentMapper {

    // 댓글 갯수
    int getListCount(int board_num);

    // 댓글 리스트 가져오기
    List<Comment> getCommentList(Map<String, Integer> map);

    int commentsInsert(Comment c);

    int commentsDelete(int num);
}
