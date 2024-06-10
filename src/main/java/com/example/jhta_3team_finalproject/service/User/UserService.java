package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.AttendanceReqeust;
import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService {

    int insert(User user);

    int getUserId(String id);

    int checkIdPw(String id, String password);

    int join(User user);

    User user_info(String id);

    int update(User user);

    int getListCount();

    User getEmployee(int userNum);

    void checkIn(int userNum);

    void checkOut(int userNum);

    //오늘의 출 퇴근 기록을 가져오는 메서드!
    Attendence  getTodayAttendance(int userNum);
    Attendence recordAttendance(int userNum, String action );
}



