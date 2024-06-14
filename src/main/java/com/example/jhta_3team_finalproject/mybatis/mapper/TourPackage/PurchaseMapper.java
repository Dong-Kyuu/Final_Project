package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PurchaseMapper {
    void insertPurchase(Purchase purchase);

    List<Purchase> getAllPurchaseInfo();

    List<Purchase> getAllPurchaseInfoByTripNo(Integer num);
}