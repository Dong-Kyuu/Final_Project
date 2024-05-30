package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import com.example.jhta_3team_finalproject.domain.Table.Table_Files;

import java.util.List;

public interface UpfilesMapper {
    void insertFile(Table_Files file);

    List<Table_Files> getFilesbyBoardNum(int boardNum);
}
