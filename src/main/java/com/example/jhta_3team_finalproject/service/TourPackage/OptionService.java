package com.example.jhta_3team_finalproject.service.TourPackage;


import com.example.jhta_3team_finalproject.domain.TourPackage.TripOption;

import java.util.List;

public interface OptionService {

    TripOption getOptionsByOptionId(String optionId);

    List<TripOption> getAllOptions();
}
