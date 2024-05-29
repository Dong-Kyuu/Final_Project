package com.example.jhta_3team_finalproject.domain.TourPackage;
public class Trip {
    String tripNo;
    String tripName;
    int tripPrice;
    int tripStock;
    int tripMaxStock;
    String regDate;
    String expireDate;
    String tripDate;
    String fileId;
    String tripMainIMG;
    String tripCategory;

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

}
