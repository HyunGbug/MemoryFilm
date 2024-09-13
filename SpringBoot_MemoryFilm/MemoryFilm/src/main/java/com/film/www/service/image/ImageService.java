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
        // 사용자가 이미지를 업로드하지 않은 경우
        if (file == null || file.isEmpty()) {
            return "/imgs/signUp/defaultProfile.png";  // 기본 이미지 경로 반환
        }

        // 파일 저장 경로 생성 및 파일 저장
        String uploadDir = getUploadDir(request);
        String uniqueFileName = generateUniqueFileName(file.getOriginalFilename(), userId);
        saveFile(file, uploadDir, uniqueFileName);

        // 저장된 파일의 URL 경로 반환 (DB에 저장할 경로)
        String profileImagePath = "/ProfileImages/" + uniqueFileName;

        // 경로를 콘솔에 출력 (디버깅용)
        System.out.println("프로필 이미지 저장 경로: " + profileImagePath);

        return profileImagePath;
    }

    // 파일 저장 경로 설정
    private String getUploadDir(HttpServletRequest request) {
        // 서버 절대 경로를 사용해 파일 저장 경로 설정
        String uploadDir = request.getServletContext().getRealPath("") + File.separator + "ProfileImages";

        // 디렉토리 존재 여부 확인 및 생성
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();  // 디렉토리가 없으면 생성
        }

        return uploadDir;
    }

    // UUID와 사용자 아이디 기반의 고유 파일 이름 생성
    private String generateUniqueFileName(String originalFilename, String userId) {
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));  // 확장자 추출
        return UUID.randomUUID().toString() + "_" + userId + extension;  // 고유 파일명 생성
    }

    // 파일 저장 처리
    private void saveFile(MultipartFile file, String uploadDir, String uniqueFileName) throws IOException {
        File destinationFile = new File(uploadDir, uniqueFileName);

        // 파일을 서버에 저장
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new IOException("파일 저장 실패: " + e.getMessage());
        }
    }
}
