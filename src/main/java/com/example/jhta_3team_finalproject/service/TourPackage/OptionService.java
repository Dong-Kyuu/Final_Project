package com.example.jhta_3team_finalproject.service.TourPackage;


import com.example.jhta_3team_finalproject.domain.TourPackage.City;
import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface OptionService {

    TripOption getOptionsByOptionId(String optionId);

    List<TripOption> getAllOptions();

    List<City> getAllCities();

    City getCityByNo(String city_no);

    String generateOptionId(String cityNo);

    void saveOption(TripOption option, MultipartFile[] images)throws IOException;

    TripOption setTripForRegAndUpdate(String optionId, String optionName, Integer optionPrice, Integer optionMaxStock, LocalDate optionDate, String cityNo);

    List<TripOption> getOptions(String optionIds);

    String mergeOptionIds(String optionIdsValue, String selectedOptions);

    String removeOptionId(String optionIds, String identifier);
}
