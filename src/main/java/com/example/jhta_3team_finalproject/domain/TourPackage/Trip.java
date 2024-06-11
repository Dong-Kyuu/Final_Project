package com.example.jhta_3team_finalproject.domain.TourPackage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Trip {
    String tripNo;

    @NotBlank(message = "상품 이름을 입력하세요.")
    String tripName;

    @NotNull(message = "상품 가격을 입력하세요.")
    @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다.")
    int tripPrice;

    int tripStock;
    int tripMaxStock;

    String regDate;
    String expireDate;
    String tripDate;
    String fileId;
    String tripMainIMG;
    String tripCategory;
    String optionIds;



    public String getTripNo() {
        return tripNo;
    }
    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }
    public String getTripName() {
        return tripName;
    }
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }
    public int getTripPrice() {
        return tripPrice;
    }
    public void setTripPrice(int tripPrice) {
        this.tripPrice = tripPrice;
    }
    public int getTripStock() {
        return tripStock;
    }
    public void setTripStock(int tripStock) {
        this.tripStock = tripStock;
    }
    public int getTripMaxStock() {
        return tripMaxStock;
    }
    public void setTripMaxStock(int tripMaxStock) {
        this.tripMaxStock = tripMaxStock;
    }
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public String getExpireDate() {
        return expireDate;
    }
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
    public String getTripDate() {
        return tripDate;
    }
    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }
    public String getFileNo() {
        return fileId;
    }
    public void setFileNo(String fileId) {
        this.fileId = fileId;
    }
    public String getTripMainIMG() {
        return tripMainIMG;
    }
    public void setTripMainIMG(String tripMainIMG) {
        this.tripMainIMG = tripMainIMG;
    }
    public String getTripCategory() {
        return tripCategory;
    }
    public void setTripCategory(String tripCategory) {
        this.tripCategory = tripCategory;
    }
    public String getOptionIds() {return optionIds;}
    public void setOptionIds(String optionIds) {this.optionIds = optionIds;}
}
