package com.example.jhta_3team_finalproject.service.board;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;
import com.example.jhta_3team_finalproject.domain.Board.Board;
import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;

import java.util.List;

public interface AnnounceBoardService {
    int getListCount(int index, String searchWord, String targetDepartment);

    List<AnnounceBoard> getBoardList(int index, String searchWord, String targetDepartment, int page, int limit);

    void insertBoard(AnnounceBoard board);

    void insertFile(int boardNum, List<BoardUpfiles> files);

    int setReadCountUpdate(int num);

    AnnounceBoard getDetail(int num);

    List<BoardUpfiles> getFilesByBoardNum(int num);

    int boardDelete(int bnum);
}
