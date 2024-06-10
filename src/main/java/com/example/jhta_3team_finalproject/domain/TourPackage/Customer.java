package com.example.jhta_3team_finalproject.domain.TourPackage;

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

    public Customer() {
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerNameKor() {
        return customerNameKor;
    }

    public void setCustomerNameKor(String customerNameKor) {
        this.customerNameKor = customerNameKor;
    }

    public String getCustomerNameEng() {
        return customerNameEng;
    }

    public void setCustomerNameEng(String customerNameEng) {
        this.customerNameEng = customerNameEng;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPassportNo() {
        return customerPassportNo;
    }

    public void setCustomerPassportNo(String customerPassportNo) {
        this.customerPassportNo = customerPassportNo;
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
