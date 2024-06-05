package com.example.jhta_3team_finalproject.mybatis.mapper.User;


import com.example.jhta_3team_finalproject.domain.User.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public User getUserId(String id);
    public int join(User user);
    public User user_info(int num);
    public int userupdate(User user);
    public int insert(User user);
    public  int getListCount();

}