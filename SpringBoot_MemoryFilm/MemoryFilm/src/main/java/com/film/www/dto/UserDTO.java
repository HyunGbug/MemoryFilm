package com.film.www.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String profileImageUrl;  // 필요할 때만 사용
    private String memo;             // 필요할 때만 사용

    // 유효성 검사를 수행하는 메서드
    public boolean isValid() {
        // 기본적인 필드 유효성 체크
        if (userId == null || userId.isEmpty()) {
            return false; // userId가 비어있으면 false
        }
        
        if (username == null || username.isEmpty()) {
            return false; // username이 비어있으면 false
        }
        
        // 유효성 체크: 이메일 형식이 올바르지 않으면 false 반환
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return false;
        }
        
        // 유효성 체크: 비밀번호가 8~20자리이며, 문자, 숫자, 특수문자가 포함되어 있는지 확인
        if (password == null || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")) {
            return false;
        }
        return true; // 모든 필드가 유효하면 true 반환
    }
}
