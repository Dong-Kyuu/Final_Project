package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Customer;
import jakarta.servlet.http.HttpSession;

public interface CustomerService {
    int join(Customer customer);

    int getcustomerId(String id);

    void update(Customer customer);

    Customer findByCustomerX(String key, String value);

    Customer getcustomerBySession(HttpSession session);
}
