package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Customer;

public interface CustomerService {
    int join(Customer customer);

    int getcustomerId(String id);

    Customer findByCustomerId(String customerId);
}
