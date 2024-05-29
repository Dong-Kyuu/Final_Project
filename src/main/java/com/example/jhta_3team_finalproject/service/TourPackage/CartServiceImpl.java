package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.*;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{

    private final CartMapper cartMapper;

    @Autowired
    public CartServiceImpl(CartMapper cartMapper) {

        this.cartMapper = cartMapper;
    }

    @Override
    public Cart getDetail(String memberId) {
        return cartMapper.getDetail(memberId);
    }

    @Override
    public int isId(String memberId) {
        return cartMapper.isId(memberId);
    }

    @Override
    public void deleteCart(String cartNo) {
        cartMapper.deleteCart(cartNo);
    }

    @Override
    public void insertCart(String memberId, String tripNo, int cartTotal, String optionIds) {
        cartMapper.insertCart(memberId,tripNo,cartTotal,optionIds);
    }

    @Override
    public void updateCart(String memberId, String tripNoValue, String newOptionIds, int totalPrice) {
        cartMapper.updateCart(memberId,tripNoValue,newOptionIds,totalPrice);
    }

    @Override
    public void deleteTripNo(String cartNo) {
        cartMapper.deleteTripNo(cartNo);
    }

    @Override
    public void deleteOption(String updatedOptionIds, String cartNo, String updatedCartTotal) {
        cartMapper.deleteOption(updatedOptionIds,cartNo,updatedCartTotal);
    }

    @Override
    public String getOptionIds(String cartNo){
        return cartMapper.getOptionIds(cartNo);
    }
}
