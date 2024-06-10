package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OptionMapper {

    TripOption getOptionsByOptionId(String optionId);

    List<TripOption> getAllOptions();
}