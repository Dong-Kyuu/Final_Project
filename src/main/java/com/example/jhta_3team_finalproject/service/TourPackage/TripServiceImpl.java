package com.example.jhta_3team_finalproject.service.TourPackage;
import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.domain.User.UserAuth;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.TripMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.UserMapper;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import com.example.jhta_3team_finalproject.service.User.UserAuthService;
import com.example.jhta_3team_finalproject.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.jhta_3team_finalproject.service.Notification.SseService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService{

    private final S3Service s3Service;
    private final TripMapper tripMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SseService sseService;
    private  final UserAuthService userAuthService;
    private final UserService userService;

    private static final String STOCK_PREFIX = "trip:stock:";
    private final UserMapper userMapper;

    @Autowired
    public TripServiceImpl(S3Service s3Service, TripMapper tripMapper, RedisTemplate<String, Object> redisTemplate, SseService sseService, UserAuthService userAuthService, UserService userService, UserMapper userMapper) {
        this.s3Service = s3Service;
        this.tripMapper = tripMapper;
        this.redisTemplate = redisTemplate;
        this.sseService = sseService;
        this.userAuthService = userAuthService;
        this.userService = userService;
        this.userMapper = userMapper;
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


    //-------------------
    //이렇게 3개 묶을만 함 + approved만 보여줘야함

    public List<Trip> getTripList2(int startRow, int endRow, String sort, String classification, String condition){
        if(classification.equals("category")){
            return tripMapper.getCategoryTripList(startRow, endRow, condition, sort);
        }else if(classification.equals("keyword")){
            return tripMapper.getTripListByKeyword(startRow, endRow, condition,sort);
        }else{
            return tripMapper.getTripList(startRow, endRow, sort);
        }
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
    //-------------------

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
        String mainIMG = images[0] != null ? s3Service.uploadFile(images[0]) : null;
        String introIMG = images[1] != null ? s3Service.uploadFile(images[1]) : null;
        String routeIMG = images[2] != null ? s3Service.uploadFile(images[2]) : null;
        String scheduleIMG = images[3] != null ? s3Service.uploadFile(images[3]) : null;
        String detailIMG = images[4] != null ? s3Service.uploadFile(images[4]) : null;

        System.out.println("fileId = "+fileId);
        System.out.println("mainIMG = "+mainIMG);

        LocalDate currentDate = LocalDate.now();

        // Trip 객체 저장
        tripMapper.insertTripFile(fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG);

        tripMapper.insertTrip(trip.getTripName(), trip.getTripPrice(),0,
                trip.getTripMaxStock(), trip.getTripDate(),String.valueOf(currentDate), trip.getExpireDate(),
                mainIMG, trip.getTripCategory(), trip.getOptionIds(), fileId);

        //SSE 알림
        int departmentNo=5;
        String message = "상품 : " + trip.getTripName() + "이 결제 대기 상태입니다.";
        String url="http://localhost:9091/trip/tripBoss";
        for(int i=3;i<5;i++){
            sseService.sendByDepartmentAndPosition(departmentNo,i,message,url);

        }

    }

    @Override
    public List<Trip> getApprovedTrip() {
        String status = "APPROVED";
        return tripMapper.getTripByStatus(status);
    }

    @Override
    public List<Trip> getPendingTrip() {
        String status = "PENDING";
        return tripMapper.getTripByStatus(status);
    }

    @Override
    public void updateTripStatus(int tripNo, String status) {
        tripMapper.updateTripStatus(tripNo,status);

        Trip trip = tripMapper.getDetail(tripNo);
        //SSE 알림
        int departmentNo=5;
        String statusKor="";
        String url="http://localhost:9091/trip/TLManagement";

        if(status.equals("APPROVED")){
            statusKor="승인";
            url += "?num="+tripNo;
        } else if (status.equals("REJECTED")) {
            statusKor="거절";
        }

        String message="상품 : " + trip.getTripName() + "이 "+statusKor+" 되었습니다.";

        for(int i=1;i<2;i++){
            sseService.sendByDepartmentAndPosition(departmentNo,i,message,url);

        }
    }

    @Override
    public boolean updateTravelLeader(int tripNo, int userNo) {
        try {
            Trip trip = tripMapper.getDetail(tripNo);
            String tripName = trip.getTripName();

            User user = userMapper.getUserByUserNo(userNo);
            String userName = user.getUsername();

            int OriginalTL = trip.getTravelleaderNo();

            int tripProgressNo = trip.getTripProgress();

            // TL업데이트
            tripMapper.updateTravelLeader(tripNo, userNo);

            //tripProgress업데이트
            if(OriginalTL==0){
                tripProgressNo +=20;
                tripMapper.updateTripProgress(tripNo,tripProgressNo);
            }

            // SSE 알림 보내기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserAuth userInfo = userAuthService.getUserInfo(authentication);

            User loginUser = (User) authentication.getPrincipal();
            int fromUserNum = loginUser.getUserNum();
            String fromUserName = loginUser.getUsername();
            String url="http://localhost:9091/trip/TLManagement?num="+tripNo;
            String message=tripName+"/"+tripNo+"의 가이드로 "+userName+"님을 지정하셨습니다.";

            sseService.sendNotification(userNo,fromUserNum,fromUserName,url,message);

            //이미 TL이 존재했던 경우
            if(OriginalTL!=0){
                message=tripName+"/"+tripNo+"의 가이드가 "+userName+"으로 변경되었습니다.";
                url="http://localhost:9091/trip/TLManagement";
                sseService.sendNotification(OriginalTL,fromUserNum,fromUserName,url,message);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateTripStock(int tripNo, int stock) {
        try {
            tripMapper.updateTripStock(tripNo, stock);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
