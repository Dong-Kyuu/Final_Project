package com.example.jhta_3team_finalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.jhta_3team_finalproject.mybatis.mapper.UserMapper;

@Service
public class UserServicelmpl implements UserService {
    private UserMapper dao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServicelmpl(UserMapper dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int isId(String id) {
        User remember = dao.isId(id);
        return (remember == null) ? -1 : 1;// -1는 아이디가 존재하지 않는 경우
        // 1은 아이디가 존재하는 경우
    }

    @Override
    public int join(User user) {
        return dao.join(user);
    }

    @Override
    public User user_info(String id) {
        return dao.isId(id);
    }


    @Override
    public int update(User user) {
        return dao.update(user);
    }
}

