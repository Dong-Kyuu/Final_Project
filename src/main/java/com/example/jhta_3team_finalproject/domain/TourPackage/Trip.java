package com.example.jhta_3team_finalproject.domain.TourPackage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString
@Setter
@Getter
public class Trip {
    private int tripNo;
    private String tripName;
    private int tripPrice;
    private int tripStock;
    private int tripMaxStock;
    private String regDate;
    private String expireDate;
    private String tripDate;
    private String fileId;
    private String tripMainImg;
    private String tripCategory;
    private String optionIds;
    private String status;
    private int travelleaderNo;

}
