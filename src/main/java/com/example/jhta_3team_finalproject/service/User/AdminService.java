package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.User;

import java.util.List;
import java.util.Map;

public interface AdminService {

    void updateUserInfo(int userNum,int departmentId,int positionId);

    List<User> getUsersFilter(int departmentId);

}
