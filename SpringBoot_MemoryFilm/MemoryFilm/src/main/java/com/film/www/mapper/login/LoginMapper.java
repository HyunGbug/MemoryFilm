package com.film.www.mapper.login;

import org.apache.ibatis.annotations.Mapper;

import com.film.www.entity.User;

@Mapper
public interface LoginMapper {
    int countById(String id);          // 아이디 중복 검사
    int countByUsername(String username); // 닉네임 중복 검사
    int countByEmail(String email);    // 이메일 중복 검사
    void insertUser(User user);        // 회원가입 처리
    User loginByIdAndPassword(String id, String password); // 로그인 처리
}

