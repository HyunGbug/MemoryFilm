package com.film.www.controller.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.film.www.service.login.LoginService;


@RestController
@RequestMapping("/auth")
public class LoginRestController {

	@Autowired
	private LoginService loginService;
	
	//회원가입 아이디 유효성 체크
	@GetMapping("/check-id")
	public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam String id){
		boolean isDuplicate = loginService.isIdDuplicate(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicate", isDuplicate);
		return ResponseEntity.ok(response);
	}
	
	//회원가입 닉네임 유효성 체크
	@GetMapping("/check-username")
	public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username){
		boolean isDuplicate = loginService.isUsernameDuplicate(username);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicate", isDuplicate);
		return ResponseEntity.ok(response);
	}
	
	//회원가입 이메일 유효성 체크
	@GetMapping("/check-email")
	public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email){
		boolean isDuplicate = loginService.isEmailDuplicate(email);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicate", isDuplicate);
		return ResponseEntity.ok(response);
	}
	
	//회원가입 처리 로직
	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupRequest signupRequest){
		 boolean success = loginService.signup(signupRequest);
	        Map<String, Object> response = new HashMap<>();
	        if (success) {
	            response.put("success", true);
	            response.put("message", "회원가입에 성공했습니다.");
	        } else {
	            response.put("success", false);
	            response.put("message", "회원가입에 실패했습니다. 다시 시도해주세요.");
	        }
	        return ResponseEntity.ok(response);
	}
}
