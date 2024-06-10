package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import com.example.jhta_3team_finalproject.domain.Board.BoardUpfiles;

import java.util.List;

public interface UpfilesMapper {
    void insertFile(BoardUpfiles file);

    List<BoardUpfiles> getFilesbyBoardNum(int boardNum);

    int deleteFile(int boardNum);

    void insertAnnounceFile(BoardUpfiles file);

    List<BoardUpfiles> getFilesbyAnnBoardNum(int num);
}
