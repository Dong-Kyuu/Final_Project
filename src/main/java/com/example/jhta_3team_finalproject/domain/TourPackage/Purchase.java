package com.example.jhta_3team_finalproject.domain.TourPackage;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class Purchase {

    private Integer id;
    private String impUid;
    private String merchantUid;
    private Integer buyerNo;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Integer tripNo;
    private String optionIds;

    private String customerNameKor;
    private String tripName;
    private String tripDate;
    private String rejectReason;

}