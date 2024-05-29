package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.OptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionMapper optionMapper;

    @Autowired
    public OptionServiceImpl(OptionMapper optionMapper) {
        this.optionMapper = optionMapper;
    }

    @Override
    public TripOption getOptionsByOptionId(String optionId) {
        return optionMapper.getOptionsByOptionId(optionId);
    }

}
