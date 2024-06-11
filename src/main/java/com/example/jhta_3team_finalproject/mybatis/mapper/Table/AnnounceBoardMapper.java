package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import com.example.jhta_3team_finalproject.domain.Board.AnnounceBoard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AnnounceBoardMapper {
    
    int getListCount(Map<String, String> map);

    List<AnnounceBoard> getBoardList(HashMap<String, Object> map);

    void insertBoard(AnnounceBoard board);

    int setReadCountUpdate(int num);

    AnnounceBoard getDetail(int num);

    int boardDelete(AnnounceBoard announceBoard);
}
