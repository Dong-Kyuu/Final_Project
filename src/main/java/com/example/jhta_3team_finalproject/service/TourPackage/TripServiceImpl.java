package com.example.jhta_3team_finalproject.service.TourPackage;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService{

    private S3Service s3Service;

    private final TripMapper tripMapper;

    @Autowired
    public TripServiceImpl(TripMapper tripMapper) {
        this.tripMapper = tripMapper;
    }

    @Override
    public int getListCount() {
        return tripMapper.getListCount();
    }

    @Override
    public int getCategoryListCount(String category) {
        return tripMapper.getCategoryListCount(category);
    }

    @Override
    public Trip getDetail(int num) {
        return tripMapper.getDetail(num);
    }

    @Override
    public List<Trip> getTripList(int startRow, int endRow, String sort) {
        return tripMapper.getTripList(startRow, endRow, sort);
    }

    @Override
    public List<Trip> getCategoryTripList(int startRow, int endRow, String category,String sort) {
        return tripMapper.getCategoryTripList(startRow, endRow, category, sort);
    }

    @Override
    public List<Trip> getTripListByKeyword(int startRow, int endRow, String keyword, String sort) {
        return tripMapper.getTripListByKeyword(startRow, endRow, keyword,sort);
    }

    @Override
    public List<Trip> getAllTrip() {
        return tripMapper.getAllTrip();
    }

    @Override
    public TripFile getTripFileByNo(String fileNo) {
        return tripMapper.getTripFileByNo(fileNo);
    }

    @Override
    public String getOptionIds(int num) {
        return tripMapper.getOptionIds(num);
    }

    @Override
    public int getKeywordListCount(String keyword) {
        return tripMapper.getKeywordListCount(keyword);
    }

    @Override
    public void saveTrip(String tripName, String tripNumber, int tripPrice, int stockNumber, String regDate, String expireDate, String category, String optionIds, MultipartFile[] images) {
        String fileId = UUID.randomUUID().toString();
        String mainIMG = s3Service.uploadFile(images[0]);
        String introIMG = s3Service.uploadFile(images[1]);
        String routeIMG = s3Service.uploadFile(images[2]);
        String scheduleIMG = s3Service.uploadFile(images[3]);
        String detailIMG = s3Service.uploadFile(images[4]);

        tripMapper.insertTrip(tripNumber, tripName, tripPrice, stockNumber, regDate, expireDate, mainIMG, category, optionIds, fileId);
        tripMapper.insertTripFile(fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG);
    }

}
