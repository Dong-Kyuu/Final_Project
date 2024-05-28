package com.example.jhta_3team_finalproject.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableCommentMapper {

    // 댓글 갯수
    int getListCount(int board_num);
}
