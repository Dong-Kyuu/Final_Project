package com.example.jhta_3team_finalproject.service;

import com.example.jhta_3team_finalproject.domain.board.Board;

import java.util.List;

public interface BoardService {

    // 글 갯수 구하기
    public int getListCount();

    // 글 목록 보기
    public List<Board> getBoardList(int page, int limit);

    // 글 등록
    void insertBoard(Board board);
}
