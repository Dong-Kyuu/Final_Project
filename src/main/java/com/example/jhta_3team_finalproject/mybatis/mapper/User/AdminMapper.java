package com.example.jhta_3team_finalproject.mybatis.mapper.User;

import com.example.jhta_3team_finalproject.domain.User.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

    void updateUserInfo(User user);

    List<User> getUsersFilter(int department_id);
}
