package com.example.jhta_3team_finalproject.mybatis.mapper.User;


import com.example.jhta_3team_finalproject.domain.User.Attendence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface AttendenceMapper {

    void CheckIn(@Param("userNum") int userNum, @Param("dateTime") LocalDateTime dateTime);

    void checkOut(@Param("userNum") int userNum, @Param("dateTime") LocalDateTime dateTime);

    //한 사람의 하루 출퇴근 기록
    Attendence getTodayAttendance(@Param("userNum") int userNum);

    //한 사람의 한달 출퇴근 기록
    List<Map<String, Object>> getMonthlyAttendances( Map<String, Object> params); //특정 직원의 한달 출퇴근 정보


    //전 직원의 한달 출퇴근 기록
    List<Map<String, Object>> getMonthlyAttendancesForAll(@Param("params") Map<String, Object> params);  // 모든 직원의 한달 출퇴근 정보를 가져옴
//    List<Attendence> getAllAttendancesByUser(int userNum);// 특정 사용자의 한 달간 출퇴근 정보를 가져옴
}