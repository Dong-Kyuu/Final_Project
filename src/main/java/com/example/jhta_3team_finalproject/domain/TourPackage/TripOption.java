package com.example.jhta_3team_finalproject.domain.TourPackage;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TripOption {
	String optionId;
	String optionName;
	int optionPrice;
	int optionStock;
	int optionMaxStock;
	String optionDate;
	String cityNo;
	String fileNo;
	String optionMainIMG;

}