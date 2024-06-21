package com.example.jhta_3team_finalproject.service.dashboard;


import com.example.jhta_3team_finalproject.domain.TourPackage.Trip;
import com.example.jhta_3team_finalproject.domain.TourPackage.TripFile;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface DashTripService {

    public int getListCount();

    public int getCategoryListCount(String category);

    public Trip getDetail(int num);

    public TripFile getTripFileByNo(String fileNo);

    public String getOptionIds(int num);

    int getKeywordListCount(String keyword);

    List<Trip> getTripList(@Param("startRow") int startRow, @Param("endRow") int endRow, @Param("sort") String sort);

    List<Trip> getCategoryTripList(@Param("startRow") int startRow, @Param("endRow") int endRow, @Param("category") String category, @Param("sort") String sort);

    List<Trip> getTripListByKeyword(@Param("startRow") int startRow, @Param("endRow") int endRow, String keyword, String sort);

    List<Trip> getAllTrip();

    void saveTrip(Trip trip,MultipartFile[] images) throws IOException;


    List<Trip> getApprovedTrip();

    List<Trip> getPendingTrip();

    void updateTripStatus(int tripNo, String status);

    boolean updateTravelLeader(int tripNo, int userNo);

    boolean updateTripStock(int tripNo, int stock);
}
