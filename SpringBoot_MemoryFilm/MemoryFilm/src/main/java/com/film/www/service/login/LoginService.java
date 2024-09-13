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

	// 회원가입 시 중복 체크 (아이디, 닉네임, 이메일)
	public void validateDuplicateUser(User user) {
		if (isIdExists(user.getUserId())) {
			throw new CustomException("아이디가 이미 존재합니다.");
		}
		if (isUsernameExists(user.getUsername())) {
			throw new CustomException("닉네임이 이미 존재합니다.");
		}
		if (isEmailExists(user.getEmail())) {
			throw new CustomException("이메일이 이미 존재합니다.");
		}
	}

	//회원가입 시 아이디 검증
	public boolean isIdExists(String userId) {
		return loginMapper.countById(userId) > 0;
	}

	//회원가입 시 닉네임 검증
	public boolean isUsernameExists(String username) {
		return loginMapper.countByUsername(username) > 0;
	}

	//회원가입 시 이멩리 검증
	public boolean isEmailExists(String email) {
		return loginMapper.countByEmail(email) > 0;
	}

	// 회원가입 처리
	public boolean signup(User user) {
		// 중복 체크 및 검증 수행
		validateDuplicateUser(user);
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
