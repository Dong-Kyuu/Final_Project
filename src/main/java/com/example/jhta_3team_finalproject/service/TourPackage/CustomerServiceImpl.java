package com.example.jhta_3team_finalproject.service.TourPackage;

import com.example.jhta_3team_finalproject.domain.TourPackage.Customer;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.TourPackage.CustomerMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.UserMapper;
import com.example.jhta_3team_finalproject.service.User.UserServicelmpl;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServicelmpl.class);
    private PasswordEncoder passwordEncoder;
    private static final int NULL_USERID = -1;


    @Autowired
    public CustomerServiceImpl(CustomerMapper customerMapper) {

        this.customerMapper = customerMapper;
    }

    @Override
    public int join(Customer customer) {
        logger.info("체크 = " + customer.toString());
        return customerMapper.join(customer);
    }

    @Override
    public int getcustomerId(String id) {
        Customer remember = customerMapper.getcustomerId(id);
        return (remember == null) ? -1 : 1;// -1는 아이디가 존재하지 않는 경우
        // 1은 아이디가 존재하는 경우
    }


    @Override
    public Customer findByCustomerX(String key, String value){
        if (key.equals("customerId")){
            return customerMapper.findByCustomerId(value);
        } else if(key.equals("customerNo")){
            return  customerMapper.findByCustomerNo(value);
        }
        return null;

    }

    @Override
    public Customer getcustomerBySession(HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer != null) {
            // customer 정보가 있는 경우 추가 작업 수행
            System.out.println("로그인된 사용자: " + customer.getCustomerId());
        } else {
            // customer 정보가 없는 경우
            System.out.println("로그인된 사용자가 없습니다.");
        }
        return customer;
    }

    @Override
    public void update(Customer customer) {
        customerMapper.update(customer);
    }
}
