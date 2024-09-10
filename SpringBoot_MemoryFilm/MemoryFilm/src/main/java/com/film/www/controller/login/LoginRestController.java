package com.film.www.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.film.www.dto.UserDTO;
import com.film.www.entity.User;
import com.film.www.service.login.LoginService;

@RestController
@RequestMapping("/auth")
public class LoginRestController {

    @Autowired
    private LoginService loginService;

    // 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDTO userDto) {
        try {
        	  User user = convertToEntity(userDto);  // UserDto를 User로 변환
              loginService.signup(user);
            return ResponseEntity.ok("회원가입에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // DTO -> Entity 변환 메서드
    private User convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return user;
    }

    // 로그인 처리
//    @PostMapping("/login")
//    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDto) {
//        try {
//        	UserDTO loggedInUser = loginService.login(userDto.getUserId(), userDto.getPassword());
//            return ResponseEntity.ok(loggedInUser);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(null);
//        }
//    }
}
