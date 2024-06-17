package com.example.jhta_3team_finalproject.service.User;

import com.example.jhta_3team_finalproject.domain.User.Attendence;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.AttendenceMapper;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.UserMapper;
import com.example.jhta_3team_finalproject.service.S3.S3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServicelmpl implements UserService {
    private final UserMapper userMapper;
    private final AttendenceMapper attendenceMapper;
    private PasswordEncoder passwordEncoder;
    private S3Service amazonS3Client;

    @Autowired
    public UserServicelmpl(UserMapper userMapper, AttendenceMapper attendenceMapper, PasswordEncoder passwordEncoder,S3Service amazonS3Client) {
        this.userMapper = userMapper;
        this.attendenceMapper = attendenceMapper;
        this.passwordEncoder = passwordEncoder;
        this.amazonS3Client= amazonS3Client;
    }


    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }

    @Override
    public User getUserId(String id) {
        return userMapper.getUserId(id);
    }

    @Override
    public int checkIdPw(String id, String password) {
        User user = userMapper.getUserId(id);
        int result = -1; // 아이디가 존재하지 않는 경우
        if (user != null) { // 아이디가 존재하는 경우
            if (passwordEncoder.matches(password, user.getPassword())) {
                result = 1; // 아이디와 비번이 일치하는 경우
            } else {
                result = 0; // 아이디는 존재하지만 비번이 일치하지 않는 경우
            }
        }
        return result;
    }

    @Override
    public User departmentPositionInfo(int department_id, int position_id) {
        return userMapper.departmentPositionInfo(department_id, position_id);
    }

    @Override
    public int join(User user) {
        return userMapper.join(user);
    }

//    @Override
//    public User user_info(User user) {
//        return userMapper.userupdate(user);
//    }

    //회원 정보 수정
    @Override
    public User getUserInfo(int userNum) {
        return userMapper.getEmployee(userNum);
    }


    @Override
    public boolean updateUser(User user, MultipartFile uploadfile) throws IOException {
        // 사용자가 새로운 프로필 사진을 업로드한 경우에만 업데이트
        if (!uploadfile.isEmpty()) {
            // 새로운 프로필 사진을 Amazon S3에 업로드하고 업로드된 파일의 URL을 가져옴
            String fileUrl = amazonS3Client.uploadFile(uploadfile);
            user.setUserProfilePicture(fileUrl); // 사용자의 프로필 사진을 업로드된 URL로 설정
        } else {
            // 사용자가 새로운 프로필 사진을 업로드하지 않은 경우, 기존의 프로필 사진 URL을 유지
            // 이전에 S3에 업로드한 URL을 가져와서 설정
            User existingUser = userMapper.getEmployee(user.getUserNum()); // 사용자 정보 불러오기
            if (existingUser != null) {
                user.setUserProfilePicture(existingUser.getUserProfilePicture()); // 기존 프로필 사진 URL 설정
            }
        }

        // 사용자 정보를 업데이트하고 업데이트 결과를 반환
        int result = userMapper.userupdate(user);
        return result == 1;
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
    public void checkIn(int userNum) {
        LocalDateTime dateTime = LocalDateTime.now();
        attendenceMapper.CheckIn(userNum, dateTime);

    }

    @Override
    public void checkOut(int userNum) {
        LocalDateTime dateTime = LocalDateTime.now();
        attendenceMapper.checkOut(userNum, dateTime);
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
        return attendenceMapper.getTodayAttendance(userNum);
    }

    @Override
    public List<Attendence> getMonthlyAttendances(int userNum, LocalDateTime startDate, LocalDateTime endDate) {
        return attendenceMapper.getMonthlyAttendances(userNum, startDate, endDate);
    }



    //신규사원 요청 처리
    @Override
    public void saveUser(User user) {
        userMapper.join(user);
    }

    @Override
    public List<User> getRegisterRequests() {
        return userMapper.findAllRequests();
    }

    @Override
    public void approveUser(int userNum) {
        userMapper.approveUser(userNum);

    }

    @Override
    public void rejectUser(int userNum) {
        userMapper.rejectUser(userNum);
    }

    @Override
    public List<User> getAllRequests() {
        return userMapper.findAllRequests();
    }

    @Override
    public List<User> getApprovedRequests() {
        return userMapper.getApprovedRequests();
    }

    @Override
    public List<User> getRejectedRequests() {
        return userMapper.getRejectedRequests();
    }

    @Override
    public Attendence getTodayAttendance(int userNum) {

        return attendenceMapper.getTodayAttendance(userNum);
    }

    @Override
    public int[] getUsersByDepartmentAndPosition(int departmentId, int positionId) {
        return userMapper.getUsersByDepartmentAndPosition(departmentId,positionId);
    }

    @Override
    public List<User> getEmployeeListByDepartment(int departmentId) {
        return userMapper.getEmployeeListByDepartment(departmentId);
    }

}
