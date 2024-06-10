package com.example.jhta_3team_finalproject.service.table;

import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;

import java.util.List;

public interface BoardService {

    // 글 갯수 구하기
    int getListCount();

    // 글 목록 보기
    List<Board> getBoardList(int page, int limit);

    // 글 등록
    void insertBoard(Board board);

    // 글 등록 시 업로드 파일 저장
    void insertFile(int boardNum, List<BoardUpfiles> files);

    // 조회수 증가
    int setReadCountUpdate(int num);

    // View 페이지
    Board getDetail(int num);

    // 업로드했던 파일 가져오기
    List<BoardUpfiles> getFilesByBoardNum(int board_num);

    // 게시글 삭제
    int boardDelete(int num);
}