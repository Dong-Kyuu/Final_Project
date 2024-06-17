package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.Response;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendenceMapper attendenceMapper;


    @Autowired
    public AttendanceServiceImpl(AttendenceMapper attendenceMapper) {
        this.attendenceMapper = attendenceMapper;
    }


    @Override
    public List<Map<String, Object>> getMonthlyAttendancesForAll(String startDateStr, String endDateStr, int department_id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = startDateStr != null ? LocalDateTime.parse(startDateStr + " 00:00:00", formatter) : null;
        LocalDateTime endDate = endDateStr != null ? LocalDateTime.parse(endDateStr + " 23:59:59", formatter) : null;

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("department_id", department_id);
        List<Map<String, Object>> attendanceList = attendenceMapper.getMonthlyAttendancesForAll(params);
        return attendanceList.stream().map(this::convertTimestampToLocalDateTime).collect(Collectors.toList());
    }

    private Map<String, Object> convertTimestampToLocalDateTime(Map<String, Object> attendance) {
        if (attendance.get("check_in_time") instanceof Timestamp) {
            attendance.put("check_in_time", ((Timestamp) attendance.get("check_in_time")).toLocalDateTime());
        }
        if (attendance.get("check_out_time") instanceof Timestamp) {
            attendance.put("check_out_time", ((Timestamp) attendance.get("check_out_time")).toLocalDateTime());
        }
        return attendance;
    }

}


//@Override
//    public LocalDateTime getStartOfMonth(String startDateStr) {
//        LocalDate startDate;
//        if (startDateStr == null) {
//            startDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
//        } else {
//            startDate = LocalDate.parse(startDateStr);
//        }
//        return startDate.atStartOfDay();
//    }
//
//    @Override
//    public LocalDateTime getEndOfMonth(String endDateStr) {
//        LocalDate endDate;
//        if (endDateStr == null) {
//            endDate = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
//        } else {
//            endDate = LocalDate.parse(endDateStr);
//        }
//        return endDate.atTime(LocalTime.MAX);
//    }
