package com.film.www.controller.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.film.www.dto.UserDTO;
import com.film.www.entity.User;
import com.film.www.service.image.ImageService;
import com.film.www.service.login.LoginService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class LoginRestController {

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private ImageService imageService;

	 // 회원가입 처리
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestParam String userId,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            HttpServletRequest request) {
    	
        try {
        	// UserDTO에서 필요한 데이터 처리
            UserDTO userDto = new UserDTO(userId, username, email, password, null, null);
            
            // 유효성 검사
            if (!userDto.isValid()) {
                return ResponseEntity.badRequest().body("모든 필드 체크를 확인해 주세요.");
            }

            // User 엔티티로 변환 - 프로필 경로 빼고
            User user = convertToEntity(userDto);

            // 프로필 이미지 처리 및 디버깅 로그 출력
            String profileImagePath = null;
            if (profilePicture != null && !profilePicture.isEmpty()) {
                profileImagePath = imageService.saveProfileImage(profilePicture, user.getUserId(), request);
            }

            // 프로필 이미지 경로 설정 (사용자가 이미지를 업로드하지 않으면 기본 이미지 사용)
            user.setProfileImageUrl(profileImagePath != null ? profileImagePath : "/imgs/signUp/defaultProfile.png");

            // 사용자 정보를 DB에 저장
            loginService.signup(user);

            return ResponseEntity.ok("회원가입에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // dto를 entity로 변환
    private User convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); // 비밀번호는 해시 처리해야 합니다.
        return user;
    }

	// 실시간 아이디 중복 확인
	@PostMapping("/check-id")
	public ResponseEntity<String> checkId(@RequestBody String userId) {
		if (loginService.isIdExists(userId)) {
			return ResponseEntity.badRequest().body("아이디가 이미 존재합니다.");
		}
		return ResponseEntity.ok("사용 가능한 아이디입니다.");
	}

	// 실시간 닉네임 중복 확인
	@PostMapping("/check-username")
	public ResponseEntity<String> checkUsername(@RequestBody String username) {
		if (loginService.isUsernameExists(username)) {
			return ResponseEntity.badRequest().body("닉네임이 이미 존재합니다.");
		}
		return ResponseEntity.ok("사용 가능한 닉네임입니다.");
	}

	// 실시간 이메일 중복 확인
	@PostMapping("/check-email")
	public ResponseEntity<String> checkEmail(@RequestBody String email) {
		if (loginService.isEmailExists(email)) {
			return ResponseEntity.badRequest().body("이메일이 이미 존재합니다.");
		}
		return ResponseEntity.ok("사용 가능한 이메일입니다.");
	}
}
