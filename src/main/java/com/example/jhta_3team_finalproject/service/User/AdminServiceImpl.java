package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Autowired
    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public void updateUserInfo(User user) {
        adminMapper.updateUserInfo(user);
    }

    @Override
    public List<User> getUsersFilter(Map<String, Object> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (params.get("startDate") != null) {
            params.put("startDate", LocalDateTime.parse(params.get("startDate") + " 00:00:00", formatter));
        }
        if (params.get("endDate") != null) {
            params.put("endDate", LocalDateTime.parse(params.get("endDate") + " 23:59:59", formatter));
        }
        return adminMapper.getUsersFilter(params);
    }
}

