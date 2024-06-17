package com.example.jhta_3team_finalproject.service.TourPackage;


import com.example.jhta_3team_finalproject.domain.TourPackage.City;
import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface OptionService {

    TripOption getOptionsByOptionId(String optionId);

    List<TripOption> getAllOptions();

    List<City> getAllCities();

    City getCityByNo(String city_no);

    String generateOptionId(String cityNo);

    void saveOption(TripOption option, MultipartFile[] images)throws IOException;
}
