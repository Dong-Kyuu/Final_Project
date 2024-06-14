package com.example.jhta_3team_finalproject.mybatis.mapper.User;


import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    public User getUserDetails(String userId);

    public User getUserId(String id);


    User departmentPositionInfo(int department_id, int position_id);

    public int join(User user);

    public User user_info(int num);

    public int userupdate(User user);

    public int insert(User user);

    public int getListCount();

    public User getEmployee(int userNum);

    //신규사원 요청 처리

    List<User> findAllRequests(); //모든 요청

    List<User> getApprovedRequests(); //승인된 요청리스트

    List<User> getRejectedRequests();//거절된 요청 리스트

    List<User> getRegisterRequests();

    void approveUser(int userNum); //사용자 승인

    void rejectUser(int userNum); //사용자 거절

}