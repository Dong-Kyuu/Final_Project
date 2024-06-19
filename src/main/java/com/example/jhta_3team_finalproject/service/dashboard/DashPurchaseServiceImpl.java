package com.example.jhta_3team_finalproject.service.dashboard;

import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.PurchaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashPurchaseServiceImpl implements DashPurchaseService {

    private final PurchaseMapper purchaseMapper;

    @Autowired
    public DashPurchaseServiceImpl(PurchaseMapper purchaseMapper) {
        this.purchaseMapper = purchaseMapper;
    }

    @Override
    public void savePurchase(Purchase purchase) {
        purchaseMapper.insertPurchase(purchase);
    }

    @Override
    public List<Purchase> getAllPurchaseInfo() {
        return purchaseMapper.getAllPurchaseInfo();
    }

    @Override
    public List<Purchase> getAllPurchaseInfoByTripNo(Integer num) {
        return purchaseMapper.getAllPurchaseInfoByTripNo(num);
    }

    @Override
    public void updatePurchaseStatus(int id, String status) {
        purchaseMapper.updatePurchaseStatus(id,status);
    }
}
