package com.example.jhta_3team_finalproject.mybatis.mapper;

import com.example.jhta_3team_finalproject.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    public User isId(String id);
    public int join(User user);
    public  User user_info (String id);
    public int update(User user);
}