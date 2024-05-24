package com.example.jhta_3team_finalproject.mybatis.mapper;

import com.example.jhta_3team_finalproject.domain.board.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {

    // 글의 갯수 구하기
    int getListCount();

    List<Board> getBoardList(HashMap<String, Integer> map);

}
