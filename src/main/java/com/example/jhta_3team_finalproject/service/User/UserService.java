package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.User;

public interface UserService {

    public int insert(User user);

    public int isId(String id);

    public int join(User user);

    public User user_info(String num);

    public int update(User user);

}
