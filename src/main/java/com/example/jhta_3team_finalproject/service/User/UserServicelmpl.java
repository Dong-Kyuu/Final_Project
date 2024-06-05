package com.example.jhta_3team_finalproject.service.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.jhta_3team_finalproject.domain.User.User;
import com.example.jhta_3team_finalproject.mybatis.mapper.User.UserMapper;

import java.util.UUID;

@Service
public class UserServicelmpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServicelmpl.class);
    private UserMapper dao;
    private PasswordEncoder passwordEncoder;
    private static final int NULL_USERID = -1;
    @Autowired
    public UserServicelmpl(UserMapper dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public int getUserId(String id) {
        User remember = dao.getUserId(id);
        return (remember == null) ? -1 : 1;// -1는 아이디가 존재하지 않는 경우
        // 1은 아이디가 존재하는 경우
    }

    @Override
    public int checkIdPw(String id ,String password) {
        User rmember = dao.getUserId(id);
        int result =NULL_USERID;//아이디가 존재하지 않는 경우 rmember=  null
        if(rmember != null) {//아이디가 존재하는 경우

            //(passwordEncoder.matches(password,rmember.getPassword()))
            //사용자에게 입력받은 패스워드를 비교하고자 할때 사용하는 메서드 입니다
            //rawPasseord:사용자가 입력한 패스워드
            //encodedPassword:DB에 저장된 패스워드

            if(passwordEncoder.matches(password,rmember.getPassword())) {
                result =1; //아이디와 비번이 일치하는 경우
            }else
                result=0;//아이디는 존재하지만 비번이 일치하는 경우
        }
        return result;
    }

    @Override
    public int join(User user) {
        logger.info("체크 = " + user.toString());
        return dao.join(user);
    }

    @Override
    public User user_info(String id) {

        return dao.getUserId(id);
    }


    @Override
    public int update(User user) {
        logger.info("업데이트 전에 로그: " + user);
        return dao.userupdate(user);
    }

    @Override
    public int insert(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword())); // 비밀번호 암호화
        return dao.insert(user);
    }

    @Override
    public int getListCount(){
        return dao.getListCount();
    }

}

