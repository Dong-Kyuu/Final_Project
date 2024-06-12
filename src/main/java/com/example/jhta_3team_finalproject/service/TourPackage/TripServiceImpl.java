package com.example.jhta_3team_finalproject.service.TourPackage;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.TripMapper;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService{

    private final S3Service s3Service;
    private final TripMapper tripMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String STOCK_PREFIX = "trip:stock:";

    @Autowired
    public TripServiceImpl(S3Service s3Service, TripMapper tripMapper, RedisTemplate<String, Object> redisTemplate) {
        this.s3Service = s3Service;
        this.tripMapper = tripMapper;
        this.redisTemplate = redisTemplate;
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
    public void saveTrip(Trip trip,MultipartFile[] images) throws IOException {

        // S3에 파일 업로드
        String fileId = UUID.randomUUID().toString();
        String mainIMG = s3Service.uploadFile(images[0]);
        String introIMG = s3Service.uploadFile(images[1]);
        String routeIMG = s3Service.uploadFile(images[2]);
        String scheduleIMG = s3Service.uploadFile(images[3]);
        String detailIMG = s3Service.uploadFile(images[4]);

        System.out.println("fileId = "+fileId);
        System.out.println("mainIMG = "+mainIMG);

        LocalDate currentDate = LocalDate.now();

        // Trip 객체 저장
        tripMapper.insertTripFile(fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG);

        tripMapper.insertTrip(trip.getTripName(), trip.getTripPrice(),0,
                trip.getTripMaxStock(), trip.getTripDate(),String.valueOf(currentDate), trip.getExpireDate(),
                mainIMG, trip.getTripCategory(), trip.getOptionIds(), fileId);

        // 재고를 Redis에 저장
        int tripNo = trip.getTripNo();
        Integer tripStock = trip.getTripStock(); // int를 Integer로 오토박싱
        redisTemplate.opsForValue().set(STOCK_PREFIX + tripNo, tripStock);
    }
/*
    //@Override
    public int getTripStock(String tripNo) {
        Integer tripStock = redisTemplate.opsForValue().get(STOCK_PREFIX + tripNo);
        return tripStock != null ? tripStock : 0;
    }

    //@Override
    public void updateTripStock(String tripNo, int tripStock) {
        redisTemplate.opsForValue().set(STOCK_PREFIX + tripNo, tripStock);
    }
*/
}
