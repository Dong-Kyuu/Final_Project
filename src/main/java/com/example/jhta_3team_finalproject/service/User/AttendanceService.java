package com.example.jhta_3team_finalproject.service.User;


import com.example.jhta_3team_finalproject.domain.User.Attendence;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface AttendanceService {

    //전체 직원의 한달 출퇴근 기록을 가져옴
    List<Attendence> getMonthlyAttendancesForAll(LocalDateTime startDateTime, LocalDateTime endDateTime);

    //시작 날짜 문자열로 받아 LocalDateTime변환
    public LocalDateTime getStartOfMonth(String startDateStr);

    //마지막 날짜 문자열로 받아 LocalDateTime변환
    public LocalDateTime getEndOfMonth(String endDateStr);


}




