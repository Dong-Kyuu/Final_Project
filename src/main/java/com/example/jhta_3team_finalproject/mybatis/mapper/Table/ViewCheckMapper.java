package com.example.jhta_3team_finalproject.mybatis.mapper.Table;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViewCheckMapper {

    int createView(int loginNum, int annboardNum);

    int ExistViewRecord(int loginNum, int annboardNum);

    int addCheck(int loginNum, int annboardNum);

    int deleteCheck(int loginNum, int annboardNum);

    int Checkedcheck(int loginNum, int annboardNum);

    void autoCheck(int userNum, int boardNum);
}
