package com.example.jhta_3team_finalproject.service.TourPackage;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService{

    private final S3Service s3Service;
    private final TripMapper tripMapper;
    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String STOCK_PREFIX = "trip:stock:";

    @Autowired
    public TripServiceImpl(S3Service s3Service, TripMapper tripMapper, RedisTemplate<String, Integer> redisTemplate) {
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
    public void saveTrip(Trip trip,MultipartFile[] images) {

        // S3에 파일 업로드
        String fileId = UUID.randomUUID().toString();
        String mainIMG = s3Service.uploadFile(images[0]);
        String introIMG = s3Service.uploadFile(images[1]);
        String routeIMG = s3Service.uploadFile(images[2]);
        String scheduleIMG = s3Service.uploadFile(images[3]);
        String detailIMG = s3Service.uploadFile(images[4]);

        // Trip 객체 저장
        tripMapper.insertTrip(trip.getTripNo(), trip.getTripName(), trip.getTripPrice(),
                trip.getTripMaxStock(), trip.getRegDate(), trip.getExpireDate(),
                mainIMG, trip.getTripCategory(), trip.getOptionIds(), fileId);
        tripMapper.insertTripFile(fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG);

        // 재고를 Redis에 저장
        redisTemplate.opsForValue().set(STOCK_PREFIX + trip.getTripNo(), trip.getTripStock());
    }

    public int getTripStock(String tripNo) {
        Integer tripStock = redisTemplate.opsForValue().get(STOCK_PREFIX + tripNo);
        return tripStock != null ? tripStock : 0;
    }

    public void updateTripStock(String tripNo, int tripStock) {
        redisTemplate.opsForValue().set(STOCK_PREFIX + tripNo, tripStock);
    }

}
