package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.City;
import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OptionMapper {

    TripOption getOptionsByOptionId(String optionId);

    List<TripOption> getAllOptions();

    List<City> getAllCities();

    City getCityByNo(String city_no);

    String getLastOptionId(String countryNo, String cityNo);

    void insertOption(String optionId, String optionName, int optionPrice, int optionStock, int optionMaxStock, String optionDate, String cityNo, String fileId,String mainIMG);
}