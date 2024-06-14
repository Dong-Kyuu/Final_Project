package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendenceMapper attendenceMapper;


    @Autowired
    public AttendanceServiceImpl(AttendenceMapper attendenceMapper) {
        this.attendenceMapper = attendenceMapper;
    }


    @Override
    public List<Attendence> getMonthlyAttendancesForAll(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDateTime);
        params.put("endDate", endDateTime);
        return attendenceMapper.getMonthlyAttendancesForAll(params);

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

    @Override
    public LocalDateTime getStartOfMonth(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        } else {
            LocalDate date = LocalDate.parse(dateStr);
            return date.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
        }
    }

    @Override
    public LocalDateTime getEndOfMonth(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        } else {
            LocalDate date = LocalDate.parse(dateStr);
            return date.with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
        }
    }
}
