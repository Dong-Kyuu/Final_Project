package com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Customer;

public interface CustomerMapper {
    int join(Customer customer);

    Customer getcustomerId(String id);

    Customer findByCustomerId(String customerId);

    void update(Customer customer);
}
