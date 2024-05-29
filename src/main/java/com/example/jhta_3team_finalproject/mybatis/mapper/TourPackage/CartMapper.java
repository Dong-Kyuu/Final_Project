package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Cart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CartMapper {

    Cart getDetail(String memberId);

    int isId(String memberId);

    void deleteCart(String cartNo);

    void insertCart(String memberId, String tripNo, int cartTotal, String optionIds);

    void updateCart(String memberId, String tripNoValue, String newOptionIds, int totalPrice);

    void deleteTripNo(String cartNo);

    void deleteOption(String updatedOptionIds, String cartNo, String updatedCartTotal);

    String getOptionIds(String cartNo);
}