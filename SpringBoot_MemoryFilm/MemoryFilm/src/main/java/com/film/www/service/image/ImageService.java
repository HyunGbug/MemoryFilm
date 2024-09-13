package com.film.www.service.image;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ImageService {

	 // 프로필 이미지를 서버에 저장하는 메서드
	public String saveProfileImage(MultipartFile file, String userId, HttpServletRequest request) throws IOException {
		//사용자가 이미지를 업로드 하지 않은 경우
		if(file == null || file.isEmpty()) {
			return "/imgs/signUp/defaultProfile.png"; //기본 프로필 이미지 경로
		}
		 // 서버 절대 경로를 사용해 파일 저장 경로 설정
        String uploadDir = request.getServletContext().getRealPath("") + File.separator + "ProfileImages";

        // 업로드 디렉토리 존재 여부 확인 및 생성
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // 디렉토리가 없으면 생성
        }

        // 파일 이름을 UUID와 사용자 아이디 기반으로 생성 (중복 방지)
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));  // 확장자 추출
        String uniqueFileName = UUID.randomUUID().toString() + "_" + userId + extension;

        // 파일 저장 경로 설정
        File destinationFile = new File(uploadDirFile, uniqueFileName);

        // 파일을 서버에 저장
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new IOException("파일 저장 실패: " + e.getMessage());
        }

        // 저장된 파일의 URL 경로 반환 (DB에 저장할 경로)
        return "/ProfileImages/" + uniqueFileName;
    }
}
