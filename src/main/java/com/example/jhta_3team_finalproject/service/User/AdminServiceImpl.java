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
    public void updateUserInfo(int userNum,int departmentId,int positionId) {
        adminMapper.updateUserInfo(userNum,departmentId,positionId);
    }

    @Override
    public List<User> getUsersFilter(int department_id) {
        return adminMapper.getUsersFilter(department_id);
    }
}

