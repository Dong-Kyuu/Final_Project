package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.AttendanceReqeust;
import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.jhta_3team_finalproject.domain.User.AttendanceReqeust;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServicelmpl implements UserService {
    private final UserMapper userMapper;
    private final AttendenceMapper attendenceMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServicelmpl(UserMapper userMapper, AttendenceMapper attendenceMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.attendenceMapper = attendenceMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public int checkIdPw(String id, String password) {
        User rmember = userMapper.getUserId(id);
        int result = -1; // 아이디가 존재하지 않는 경우
        if (rmember != null) { // 아이디가 존재하는 경우
            if (passwordEncoder.matches(password, rmember.getPassword())) {
                result = 1; // 아이디와 비번이 일치하는 경우
            } else {
                result = 0; // 아이디는 존재하지만 비번이 일치하지 않는 경우
            }
        }
        return result;
    }

    @Override
    public User departmentPositionInfo(int department_id, int position_id) {
        return userMapper.departmentPositionInfo(department_id,position_id);
    }

    @Override
    public int join(User user) {
        return userMapper.join(user);
    }

    @Override
    public User user_info(String id) {
        return userMapper.getUserId(id);
    }

    @Override
    public int update(User user) {
        return userMapper.userupdate(user);
    }

    @Override
    public int getListCount() {
        return userMapper.getListCount();
    }

    @Override
    public User getEmployee(int userNum) {
        return userMapper.getEmployee(userNum);
    }

    @Override
    public int getUserId(String id) {
        User remember = userMapper.getUserId(id);
        return (remember == null) ? -1 : 1; // -1은 아이디가 존재하지 않는 경우, 1은 아이디가 존재하는 경우
    }
    @Override
    public void checkIn(int userNum) {
        LocalDateTime dateTime =LocalDateTime.now();
        attendenceMapper.CheckIn(userNum,dateTime);

    }

    @Override
    public void checkOut(int userNum){
        LocalDateTime dateTime=LocalDateTime.now();
        attendenceMapper.checkOut(userNum,dateTime);
    }

    @Override
    public Attendence recordAttendance(int userNum, String action) {
        LocalDateTime dateTime = LocalDateTime.now();

        if ("CheckIn".equalsIgnoreCase(action)) {
            //이미 해당 날에 출근 이력이 있는지 확인 -> 없으면 아래 실행 있으면
            attendenceMapper.CheckIn(userNum, dateTime);

        } else if ("checkOut".equalsIgnoreCase(action)) {
            attendenceMapper.checkOut(userNum, dateTime);
        }
        return  attendenceMapper.getTodayAttendance(userNum);
    }

    @Override
    public List<Attendence> getMonthlyAttendances(int userNum){
        return attendenceMapper.getMonthlyAttendances(userNum);
    }

    @Override
    public Attendence getTodayAttendance(int userNum){
         return  attendenceMapper.getTodayAttendance(userNum);
    }


}
