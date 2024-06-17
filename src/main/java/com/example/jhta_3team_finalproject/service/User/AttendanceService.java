package com.example.jhta_3team_finalproject.service.User;


import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface AttendanceService {

    //전체 직원의 출퇴근 기록을 가져옴
    List<Map<String, Object>> getMonthlyAttendancesForAll(String startDateStr, String endDateStr, int department_id);


}




