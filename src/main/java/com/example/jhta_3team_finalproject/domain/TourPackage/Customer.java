package com.example.jhta_3team_finalproject.domain.TourPackage;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Customer {
    private int customerNo;
    private String customerId;
    private String customerPassword;
    private String customerNameKor;
    private String customerNameEng;
    private String customerGender;
    private String customerEmail;
    private String customerPhone;
    private String customerPassportNo;
    private String tripNo;
    private String tripGroup;


    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerNo=" + customerNo +
                ", customerId='" + customerId + '\'' +
                ", customerPassword='" + customerPassword + '\'' +
                ", customerNameKor='" + customerNameKor + '\'' +
                ", customerNameEng='" + customerNameEng + '\'' +
                ", customerGender=" + customerGender +
                ", customerEmail='" + customerEmail + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", customerPassportNo='" + customerPassportNo + '\'' +
                '}';
    }
}
