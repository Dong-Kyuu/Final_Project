package com.example.jhta_3team_finalproject.controller;

import com.example.jhta_3team_finalproject.domain.TourPackage.Purchase;
import com.example.jhta_3team_finalproject.domain.TourPackage.Trip;
import com.example.jhta_3team_finalproject.service.TourPackage.PurchaseService;
import com.example.jhta_3team_finalproject.service.TourPackage.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/chart")
public class ChartController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private TripService tripService;

    @GetMapping("/categorySales")
    public ResponseEntity<Map<String, Integer>> getCategorySales() {
        List<Purchase> purchases = purchaseService.getAllPurchaseInfo();
        Trip trip;
        String category;

        Map<String, Integer> categorySales = new HashMap<>();
        categorySales.put("WEU", 0);
        categorySales.put("SEU", 0);
        categorySales.put("EEU", 0);
        categorySales.put("CEU", 0);

        int tripNo;
        for (Purchase purchase : purchases) {
            tripNo=purchase.getTripNo();
            trip=tripService.getDetail(tripNo);
            category=trip.getTripCategory();

            if (categorySales.containsKey(category)) {
                categorySales.put(category, categorySales.get(category) + 1);
            }
        }

        return ResponseEntity.ok(categorySales);
    }

    @GetMapping("/monthlySales")
    public ResponseEntity<Map<Integer, Integer>> getMonthlySales() {
        List<Purchase> purchases = purchaseService.getAllPurchaseInfo();
        Trip trip;
        String category;

        Map<Integer, Integer> monthlySales = new HashMap<>();

        for(int i=1;i<=12;i++){
            monthlySales.put(i, 0);
        }

        int tripNo;
        int key;

        for (Purchase purchase : purchases) {
            tripNo=purchase.getTripNo();
            trip=tripService.getDetail(tripNo);
            String tripDate = trip.getTripDate();

            key=this.getDateSplit(tripDate,"month");

            if (monthlySales.containsKey(key)) {
                monthlySales.put(key, monthlySales.get(key) + 1);
            }
        }

        return ResponseEntity.ok(monthlySales);
    }

    @GetMapping("/dailyIncomeSales")
    public ResponseEntity<Map<String, Integer>> getDailyIncomeSales() {
        System.out.println("dailyIncomeSalesChart");

        List<Purchase> purchases = purchaseService.getAllPurchaseInfo();
        Trip trip;

        Map<String, Integer> dailyIncome = new HashMap<>();

        int tripNo;
        int amount;

        for (Purchase purchase : purchases) {
            String date = purchase.getCreatedAt().toLocalDate().toString();

            // Use BigDecimal to avoid issues with string conversion and decimal places
            BigDecimal amountDecimal = new BigDecimal(String.valueOf(purchase.getAmount()));
            amount = amountDecimal.intValue(); // This will give the integer part, effectively truncating the decimal part

            // Add the amount to the corresponding day
            dailyIncome.put(date, dailyIncome.getOrDefault(date, 0) + amount);
        }

        Map<String, Integer> sortedDailyIncome = new TreeMap<>(dailyIncome);

        return ResponseEntity.ok(sortedDailyIncome);
    }

    @GetMapping("/monthlyIncomeSales")
    public ResponseEntity<Map<Integer, Integer>> getMonthlyIncomeSales() {

        System.out.println("monthlyIncomeSalesChart");

        List<Purchase> purchases = purchaseService.getAllPurchaseInfo();
        Trip trip;
        String category;

        Map<Integer, Integer> monthlyIncome = new HashMap<>();

        for(int i=1;i<=12;i++){
            monthlyIncome.put(i, 0);
        }

        int tripNo;
        int key;
        String amountString;
        String amountString1;
        int amount;

        for (Purchase purchase : purchases) {
            tripNo=purchase.getTripNo();
            trip=tripService.getDetail(tripNo);
            String tripDate = trip.getTripDate();

            key=this.getDateSplit(tripDate,"month");

            BigDecimal amountDecimal = new BigDecimal(String.valueOf(purchase.getAmount()));
            amount = amountDecimal.intValue();

            if (monthlyIncome.containsKey(key)) {
                monthlyIncome.put(key, monthlyIncome.get(key) + amount);
            }
        }

        return ResponseEntity.ok(monthlyIncome);
    }



    public int getDateSplit(String date,String condition){
        //년,월,일 로 분리
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];

        int yearDate = Integer.parseInt(year);
        int monthDate = Integer.parseInt(month);
        int dayDate = Integer.parseInt(day);

        return switch (condition) {
            case "year" -> yearDate;
            case "month" -> monthDate;
            case "day" -> dayDate;
            default -> 0;
        };
    }


}