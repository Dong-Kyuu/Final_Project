package com.example.jhta_3team_finalproject.service.table;

import com.example.jhta_3team_finalproject.mybatis.mapper.BoardMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.TableCommentMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.UpfilesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
