package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.User;

public interface UserService {

    public int insert(User user);

    public int getUserId(String id);

    int checkIdPw(String id, String password);

    public int join(User user);

    public User user_info(String id);

    public int update(User user);

    public int getListCount();

    public User getEmployee(int userNum);
}
