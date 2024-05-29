package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OptionMapper {

    TripOption getOptionsByOptionId(String optionId);
}