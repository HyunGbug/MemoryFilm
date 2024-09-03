package com.film.www.service.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.film.www.mapper.login.LoginMapper;

@Service
public class LoginService {

	@Autowired
	private LoginMapper loginMapper;

	public boolean isIdDuplicate(String id) {
		return loginMapper.countById(id) > 0;
	}

	public boolean isUsernameDuplicate(String username) {
		return loginMapper.countByUserName(username) > 0;
	}

	public boolean isEmailDuplicate(String email) {
		return loginMapper.countByEmail(email) > 0;
	}

	public boolean signup(SignupRequest signupRequest) {
		// 비밀번호 유효성 검사, 기타 로직을 추가할 수 있습니다.
		int result = loginMapper.insertUser(signupRequest.toUserEntity());
		return result > 0;
	}
}
