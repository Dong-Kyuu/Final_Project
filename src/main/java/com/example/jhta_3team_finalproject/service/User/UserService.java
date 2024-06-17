package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.AttendanceReqeust;
import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {

    int insert(User user);

    User getUserId(String id);

    int checkIdPw(String id, String password);

    User departmentPositionInfo(int department_id, int position_id);

    int join(User user);

//    User user_info(String id);

//    int update(User user);

    boolean updateUser(User user, MultipartFile uploadfile) throws IOException;

//    User user_info(User user);

    int getListCount();

    User getEmployee(int userNum);

    void checkIn(int userNum);

    void checkOut(int userNum);

    User getUserInfo(int userNum);

    //오늘의 출 퇴근 기록을 가져오는 메서드!
    Attendence getTodayAttendance(int userNum);

    Attendence recordAttendance(int userNum, String action);

    //한 사람의 출퇴근 한달 정보를 가져오는 메서드
    List<Map<String, Object>> getMonthlyAttendances(int userNum, String startDate, String endDate);

    //직원 전체의 출퇴근 정보를 가져오는 메서드

    //신규사원 요청 처리
    void saveUser(User user);


    void approveUser(int userNum); //요청 승인

    void rejectUser(int userNum);// 요청 거절

    //이거
    public List<Map<String, Object>> getUsersFilter(int user_is_approved); //모든 요청


}

