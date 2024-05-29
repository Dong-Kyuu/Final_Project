package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripMapper {

    int getListCount();

    int getCategoryListCount(String category);

    int getKeywordListCount(String keyword);

    Trip getDetail(int num);

    TripFile getTripFileByNo(String fileNo);

    String getOptionIds(int num);

    List<Trip> getCategoryTripList(int startRow, int endRow, String category,String sort);

    List<Trip> getTripList(int startRow, int endRow, String sort);

    List<Trip> getTripListByKeyword(int startRow, int endRow, String keyword, String sort);
}

