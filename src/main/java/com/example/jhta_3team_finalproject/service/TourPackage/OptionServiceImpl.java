package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.OptionMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.TripMapper;
import com.example.jhta_3team_finalproject.service.Notification.SseService;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionMapper optionMapper;
    private final S3Service s3Service;
    private final TripMapper tripMapper;
    private final SseService sseService;


    @Autowired
    public OptionServiceImpl(OptionMapper optionMapper, S3Service s3Service, TripMapper tripMapper, SseService sseService) {
        this.optionMapper = optionMapper;
        this.s3Service = s3Service;
        this.tripMapper = tripMapper;
        this.sseService = sseService;
    }

    @Override
    public TripOption getOptionsByOptionId(String optionId) {
        return optionMapper.getOptionsByOptionId(optionId);
    }

    @Override
    public List<TripOption> getAllOptions() {
        return optionMapper.getAllOptions();
    }

    @Override
    public List<City> getAllCities() {
        return optionMapper.getAllCities();
    }

    @Override
    public City getCityByNo(String city_no) {
        return optionMapper.getCityByNo(city_no);
    }

    @Override
    public String generateOptionId(String cityNo) {

        City city = optionMapper.getCityByNo(cityNo);
        String countryNo = city.getCountryNo();

        String lastOptionId = optionMapper.getLastOptionId(countryNo, cityNo);
        int newSequence = 1;

        if (lastOptionId != null) {
            String sequenceStr = lastOptionId.substring(0,2);
            System.out.println("sequenceStr = "+sequenceStr);
            newSequence = Integer.parseInt(sequenceStr) + 1;
        }

        String newOptionId = String.format("%02d%s", newSequence, cityNo);
        System.out.println("newOptionId = "+newOptionId);

        return newOptionId;

    }

    @Override
    public void saveOption(TripOption option, MultipartFile[] images) throws IOException {
        // S3에 파일 업로드
        String fileId = UUID.randomUUID().toString();

        String mainIMG = images[0] != null ? s3Service.uploadFile(images[0]) : null;
        String introIMG =  null;
        String routeIMG =  null;
        String scheduleIMG =  null;
        String detailIMG =  null;

        System.out.println("fileId = "+fileId);
        System.out.println("mainIMG = "+mainIMG);

        LocalDate currentDate = LocalDate.now();

        //option객체 저장
        tripMapper.insertTripFile(fileId, mainIMG, introIMG, routeIMG, scheduleIMG, detailIMG);
        optionMapper.insertOption(option.getOptionId(),option.getOptionName(),
                option.getOptionPrice(),0,option.getOptionMaxStock(),
                option.getOptionDate(),option.getCityNo(),fileId,mainIMG);

        // SSE 알림 보내기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loginUser = (User) authentication.getPrincipal();
        int userNum = loginUser.getUserNum();
        System.out.println("userNum = "+userNum);
        String message = "상품 : " + option.getOptionName() + "이 결제 대기 상태입니다.";
        sseService.sendNotification(userNum,userNum,"san","http://localhost:9091/tripDepartment", message);


    }

    @Override
    public TripOption setTripForRegAndUpdate(String optionId,String optionName, Integer optionPrice, Integer optionMaxStock, LocalDate optionDate, String cityNo) {
        TripOption option = new TripOption();
        option.setOptionId(optionId);
        option.setOptionName(optionName);
        option.setOptionPrice(optionPrice);
        option.setOptionMaxStock(optionMaxStock);
        option.setOptionDate(optionDate.toString());
        option.setCityNo(cityNo);
        return option;
    }

    @Override
    public List<TripOption> getOptions(String optionIds) {
        List<TripOption> options = new ArrayList<>();
        String[] optionIdArray = optionIds.split("-");
        for (String optionId : optionIdArray) {
            TripOption option = optionMapper.getOptionsByOptionId(optionId);
            if (option != null) {
                options.add(option);
            }
        }
        return options;
    }

    @Override
    public String mergeOptionIds(String existingOptionIds, String newOptionIds) {
        if (existingOptionIds == null || existingOptionIds.equals("null")) {
            return newOptionIds;
        }
        String[] existingOptions = existingOptionIds.split("-");
        String[] newOptions = newOptionIds.split("-");

        StringBuilder mergedOptions = new StringBuilder(existingOptionIds);
        for (String newOption : newOptions) {
            boolean exists = false;
            for (String existingOption : existingOptions) {
                if (newOption.equals(existingOption)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                mergedOptions.append("-").append(newOption);
            }
        }
        return mergedOptions.toString();
    }

    @Override
    public String removeOptionId(String optionIds, String optionId) {    // optionIds에서 optionId를 제거하는 메서드
        String[] optionIdArray = optionIds.split("-");
        StringBuilder sb = new StringBuilder();

        for (String id : optionIdArray) {
            if (!id.equals(optionId)) {
                sb.append(id).append("-");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // 마지막의 "-"를 삭제합니다.
        }

        return sb.toString();
    }
}
