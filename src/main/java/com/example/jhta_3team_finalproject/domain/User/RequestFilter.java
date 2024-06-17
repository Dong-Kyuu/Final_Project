//package com.example.jhta_3team_finalproject.domain.User;
//
//import com.example.jhta_3team_finalproject.domain.User.Attendence;
//import com.example.jhta_3team_finalproject.service.User.AttendanceService;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//public enum RequestFilter {
//
//    ALL("전 직원") {
//        @Override
//        public List<Map<String, Object>> apply(AttendanceService attendanceService, String startDate, String endDate) {
//            return attendanceService.getMonthlyAttendancesForAll(startDate, endDate);
//        }
//    };
//
//    private final String logMessage;
//
//    RequestFilter(String logMessage) {
//        this.logMessage = logMessage;
//    }
//
//    public abstract List<Map<String, Object>>  apply(AttendanceService attendanceService, String startDate, String endDate);
//
//    public String getLogMessage() {
//        return logMessage;
//    }
//}
//
///*
//
//
// HumanResources("인사부") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("HumanResources", startDate, endDate);
//            return null;
//        }
//    },
//    Administration("관리부") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("Administration", startDate, endDate);
//            return null;
//        }
//    },
//    PublicRelations("홍보부") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("PublicRelations", startDate, endDate);
//            return null;
//        }
//    },
//    Support("지원부") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("Support", startDate, endDate);
//            return null;
//        }
//    },
//    Sales("영업부") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("Sales", startDate, endDate);
//            return null;
//        }
//    },
//    Executives("임원") {
//        @Override
//        public List<Attendence> apply(AttendanceService attendanceService, LocalDateTime startDate, LocalDateTime endDate) {
////            return attendanceService.getMonthlyAttendancesByDepartment("Executives", startDate, endDate);
//            return null;
//        }
//    },
// */