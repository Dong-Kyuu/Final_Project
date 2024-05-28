package com.example.jhta_3team_finalproject.mybatis.mapper;

import com.example.jhta_3team_finalproject.domain.Table.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BoardMapper {

    // 글의 갯수 구하기
    int getListCount();

    // 리스트 불러오기
    List<Board> getBoardList(HashMap<String, Integer> map);

    // 글 등록하기
    void insertBoard(Board board);

    // 조회수 증가
    int setReadCountUpdate(int num);

    // View 페이지 가져오기
    Board getDetail(int num);
}
