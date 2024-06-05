package com.example.jhta_3team_finalproject.service.table;

import com.example.jhta_3team_finalproject.domain.Board.BoardComment;
import com.example.jhta_3team_finalproject.mybatis.mapper.Table.TableCommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableCommentServiceImpl implements TableCommentService {

    private TableCommentMapper dao;

    @Autowired
    public TableCommentServiceImpl(TableCommentMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getListCount(int board_num) {
        return dao.getListCount(board_num);
    }

    @Override
    public List<BoardComment> getCommentList(int board_num, int page) {
        int startrow = 0;
        int endrow = page * 5;      // 5 10 15
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("board_num", board_num);
        map.put("start", startrow);
        map.put("end", endrow);

        return dao.getCommentList(map);
    }

    @Override
    public int commentsInsert(BoardComment c) {
        return dao.commentsInsert(c);
    }

    @Override
    public int commentsDelete(int num) {
        return dao.commentsDelete(num);
    }


}
