package com.example.jhta_3team_finalproject.domain.TourPackage;

public class Cart {
	String cartNo;
	String tripNo;
	String optionIds;
	int cartTotal;
	int cartoptionTotal;
	
	public String getCartNo() {
		return cartNo;
	}
	public void setCartNo(String cartNo) {
		this.cartNo = cartNo;
	}
	public String getTripNo() {
		return tripNo;
	}
	public void setTripNo(String tripNo) {
		this.tripNo = tripNo;
	}
	public String getOptionIds() {
		return optionIds;
	}
	public void setOptionIds(String optionIds) {
		this.optionIds = optionIds;
	}
	public int getCartTotal() {
		return cartTotal;
	}
	public void setCartTotal(int cartTotal) {
		this.cartTotal = cartTotal;
	}
	public int getCartoptionTotal() {
		return cartoptionTotal;
	}
	public void setCartoptionTotal(int cartoptionTotal) {
		this.cartoptionTotal = cartoptionTotal;
	}
	
	
}
