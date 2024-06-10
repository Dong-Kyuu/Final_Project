package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import com.example.jhta_3team_finalproject.domain.Board.Board;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {

    // 글의 갯수 구하기
    int getListCount(Map<String, String> map);

    // 리스트 불러오기
    List<Board> getBoardList(HashMap<String, Object> map);

    // 글 등록하기
    void insertBoard(Board board);

    // 조회수 증가
    int setReadCountUpdate(int num);

    // View 페이지 가져오기
    Board getDetail(int num);

    // 게시글 삭제
    int boardDelete(Board board);

    // 게시글 수정
    int boardModify(Board bdata);
}
