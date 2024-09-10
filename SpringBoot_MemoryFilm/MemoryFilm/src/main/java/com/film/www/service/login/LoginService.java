package com.film.www.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.film.www.entity.User;
import com.film.www.exception.CustomException;
import com.film.www.mapper.login.LoginMapper;

@Service
public class LoginService {

    @Autowired
    private LoginMapper loginMapper;

    // 회원가입 처리
    public boolean signup(User user) {
        if (loginMapper.countById(user.getUserId()) > 0) {
            throw new CustomException("아이디가 이미 존재합니다.");
        }
        if (loginMapper.countByUsername(user.getUsername()) > 0) {
            throw new CustomException("닉네임이 이미 존재합니다.");
        }
        if (loginMapper.countByEmail(user.getEmail()) > 0) {
            throw new CustomException("이메일이 이미 존재합니다.");
        }

        // 사용자 등록
        loginMapper.insertUser(user);
        return true;
    }

    // 로그인 처리
    public User login(String id, String password) {
        User user = loginMapper.loginByIdAndPassword(id, password);
        if (user == null) {
            throw new CustomException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        return user;
    }
}
