package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface TripService {

    public int getListCount();

    public int getCategoryListCount(String category);

    public Trip getDetail(int num);

    public TripFile getTripFileByNo(String fileNo);

    public String getOptionIds(int num);

    int getKeywordListCount(String keyword);

    List<Trip> getTripList(@Param("startRow") int startRow, @Param("endRow") int endRow, @Param("sort") String sort);

    List<Trip> getCategoryTripList(@Param("startRow") int startRow, @Param("endRow") int endRow, @Param("category") String category, @Param("sort") String sort);

    List<Trip> getTripListByKeyword(@Param("startRow") int startRow, @Param("endRow") int endRow, String keyword, String sort);
}
