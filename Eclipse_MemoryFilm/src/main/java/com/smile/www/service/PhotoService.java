package com.smile.www.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;

import com.smile.www.dao.PhotoDAO;
import com.smile.www.dto.PhotoDTO;

public class PhotoService {
	private PhotoDAO photoDAO;

	public PhotoService() {
		this.photoDAO = new PhotoDAO();
	}

	// 세션, 속성 검증하기
	private Integer getUserNoFromSession(HttpSession session) {
		if (session == null || session.getAttribute("userNo") == null) {
			return null;
		}
		return (Integer) session.getAttribute("userNo");
	}

	// 공통으로 사진 목록을 불러와 세션에 저장하는 메서드
	protected void updatePhotoListInSession(HttpSession session, int userNo) throws SQLException {
		List<PhotoDTO> photos = photoDAO.getPhotosByUser(userNo);
		Map<String, List<PhotoDTO>> groupedPhotos = new TreeMap<>(Collections.reverseOrder());

		for (PhotoDTO photo : photos) {
			String date = photo.getUploadDate().split(" ")[0]; // 날짜 부분만 사용
			if (!groupedPhotos.containsKey(date)) {
				groupedPhotos.put(date, new ArrayList<>());
			}
			groupedPhotos.get(date).add(photo);
		}
		session.setAttribute("groupedPhotos", groupedPhotos);
	}

	// 업로드 사진 추가
	public String addPhoto(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			return "/ErrorFile/sessionError.jsp";
		}

		try {
			List<Part> fileParts = (List<Part>) request.getParts();
			String uploadDir = request.getServletContext().getRealPath("") + File.separator + "UploadedPhotos";

			File uploadDirFile = new File(uploadDir);
			if (!uploadDirFile.exists()) {
				uploadDirFile.mkdirs();
			}

			for (Part filePart : fileParts) {
				if (filePart.getName().equals("images") && filePart.getSize() > 0) {
					String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
					String uniqueFileName = generateUniqueFileName(uploadDir, originalFileName, userNo);
					File file = new File(uploadDir, uniqueFileName);

					try (InputStream input = filePart.getInputStream()) {
						Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
					}
					// 로그에 파일 경로 출력
					System.out.println("Uploaded file path: " + file.getAbsolutePath());

					PhotoDTO photo = new PhotoDTO();
					photo.setUserNo(userNo);
					photo.setFilePath("UploadedPhotos/" + uniqueFileName);
					photoDAO.addPhoto(photo);
				}
			}
		} catch (SizeLimitExceededException e) {
			e.printStackTrace();
			return "/FileSizeExceeded";
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			return "/ErrorFile/uploadPhotoError.jsp";
		}
		// 사진 목록을 다시 불러와 세션에 저장
		updatePhotoListInSession(session, userNo);
		return "/Html/HomePage(Login)_UI.jsp"; // 직접 포워딩하여 리다이렉트 문제 해결
	}

	// 파일 이름 생성하는 메서드
	private String generateUniqueFileName(String uploadDir, String fileName, int userNo) {
		String extension = fileName.substring(fileName.lastIndexOf('.'));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String date = sdf.format(new Date());
		String uuid = UUID.randomUUID().toString().substring(0, 8); // UUID의 앞 8자만 사용
		String uniqueFileName = "user" + userNo + "_" + date + "_" + uuid + extension;
		File file = new File(uploadDir, uniqueFileName);
		int count = 1;
		while (file.exists()) {
			uniqueFileName = "user" + userNo + "_" + date + "_" + uuid + "(" + count + ")" + extension;
			file = new File(uploadDir, uniqueFileName);
			count++;
		}
		return uniqueFileName;
	}

	// 사진 하나 다운로드
	public void downloadPhoto(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String photoNo = request.getParameter("no");
		if (photoNo == null || photoNo.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing photo ID");
			return;

		}

		int photoId;
		try {
			photoId = Integer.parseInt(photoNo);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid photo ID");
			return;
		}

		String filePath;
		try {
			filePath = photoDAO.getPhotoPathByPhotoNo(photoId);
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
			return;
		}

		if (filePath == null || filePath.isEmpty()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Photo not found");
			return;
		}

		String realPath = request.getServletContext().getRealPath("") + File.separator + filePath;
		File file = new File(realPath);
		if (!file.exists()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
			return;
		}

		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

		try (FileInputStream fileInputStream = new FileInputStream(file);
				ServletOutputStream outputStream = response.getOutputStream()) {

			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}
	}

	// 사용자가 선택한 사진들을 다운로드해 압축해서 제공
	public void downloadSelectedPhotos(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			response.sendRedirect(request.getContextPath() + "/ErrorFile/sessionError.jsp");
			return;
		}

		String[] photoNoStrings = request.getParameterValues("photoNos");
		if (photoNoStrings == null || photoNoStrings.length == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No photos selected for download.");
			return;
		}

		List<Integer> photoNos = Arrays.stream(photoNoStrings).filter(s -> s != null && !s.trim().isEmpty()) // 빈 문자열과
				.map(Integer::parseInt).collect(Collectors.toList());

		if (photoNos.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No valid photos selected for download.");
			return;
		}

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment; filename=\"FilmMemoryPhotos.zip\"");

		try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
			for (int photoNo : photoNos) {
				String filePath = photoDAO.getPhotoPathByPhotoNo(photoNo);
				if (filePath == null) {
					continue;
				}

				File file = new File(request.getServletContext().getRealPath("") + File.separator + filePath);
				if (!file.exists()) {
					continue;
				}

				try (FileInputStream fis = new FileInputStream(file)) {
					ZipEntry zipEntry = new ZipEntry(file.getName());
					zos.putNextEntry(zipEntry);

					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, bytesRead);
					}
					zos.closeEntry();
				}
			}

		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
		}
	}

	// 사용자 별로 업로드된 모든 사진을 목록 형태로 보여준다.
	public String listPhotos(HttpServletRequest request) throws SQLException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			return "/ErrorFile/sessionError.jsp";
		}

		updatePhotoListInSession(session, userNo);
		return "/Html/HomePage(Login)_UI.jsp";
	}

	// 사진 개수 가져오는 메서드
	public void getPhotoCount(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			response.sendRedirect(request.getContextPath() + "/ErrorFile/sessionError.jsp");
			return;
		}

		try { // 사용자별 업로드한 사진 개수 가져오기
			int photoCount = photoDAO.getPhotoCountByUser(userNo);
			session.setAttribute("photoCount", photoCount);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("{\"photoCount\": " + session.getAttribute("photoCount") + "}");
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/ErrorFile/error.jsp");
		}
	}

	// 특정 사진의 상세 정보를 보여주는 기능이다.
	public void viewPhoto(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session not found");
			} catch (IOException e) {

				e.printStackTrace();
			}
			return;
		}

		int photoNo = Integer.parseInt(request.getParameter("no"));
		PhotoDTO photo = photoDAO.getPhoto(photoNo);
		if (photo == null) {
			try {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "사진을 찾을 수 없음");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		 String jsonResponse = String.format(
			        "{\"no\":%d,\"userNo\":%d,\"filePath\":\"%s\",\"uploadDate\":\"%s\",\"cameraInfo\":\"%s\",\"lensInfo\":\"%s\",\"locationInfo\":\"%s\",\"memo\":\"%s\"}",
			        photo.getNo(),
			        photo.getUserNo(),
			        escapeJson(photo.getFilePath()),
			        escapeJson(photo.getUploadDate()),
			        escapeJson(photo.getCameraInfo()),
			        escapeJson(photo.getLensInfo()),
			        escapeJson(photo.getLocationInfo()),
			        escapeJson(photo.getMemo())
			    );

			    response.setContentType("application/json");
			    response.setCharacterEncoding("UTF-8");
			    try {
			        response.getWriter().write(jsonResponse);
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
		/*
		 * String jsonResponse = "{"; jsonResponse += "\"no\":" + photo.getNo() + ",";
		 * jsonResponse += "\"userNo\":" + photo.getUserNo() + ","; jsonResponse +=
		 * "\"filePath\":\"" + (photo.getFilePath() != null ? photo.getFilePath() : "")
		 * + "\","; jsonResponse += "\"uploadDate\":\"" + (photo.getUploadDate() != null
		 * ? photo.getUploadDate() : "") + "\","; jsonResponse += "\"cameraInfo\":\"" +
		 * (photo.getCameraInfo() != null ? photo.getCameraInfo() : "") + "\",";
		 * jsonResponse += "\"lensInfo\":\"" + (photo.getLensInfo() != null ?
		 * photo.getLensInfo() : "") + "\","; jsonResponse += "\"locationInfo\":\"" +
		 * (photo.getLocationInfo() != null ? photo.getLocationInfo() : "") + "\",";
		 * jsonResponse += "\"memo\":\"" + (photo.getMemo() != null ? photo.getMemo() :
		 * "") + "\""; jsonResponse += "}";
		 * 
		 * response.setContentType("application/json"); try {
		 * response.getWriter().write(jsonResponse); } catch (IOException e) {
		 * 
		 * e.printStackTrace(); }
		 */
	}
	private String escapeJson(String value) {
	    if (value == null) {
	        return "";
	    }
	    return value.replace("\\", "\\\\")
	                .replace("\"", "\\\"")
	                .replace("\b", "\\b")
	                .replace("\f", "\\f")
	                .replace("\n", "\\n")
	                .replace("\r", "\\r")
	                .replace("\t", "\\t");
	}

	// 사진 정보, 메모 등 업데이트
	public void savePhotoDetails(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session not found");
			} catch (IOException e) {

				e.printStackTrace();
			}
			return;
		}
		int photoNo = Integer.parseInt(request.getParameter("photoNo")); // Integer로 하는 익ㄴ
		PhotoDTO photo = new PhotoDTO();
		photo.setNo(photoNo);
		photo.setCameraInfo(request.getParameter("camera"));
		photo.setLensInfo(request.getParameter("film"));
		photo.setLocationInfo(request.getParameter("location"));
		photo.setMemo(request.getParameter("memo"));

		boolean result = photoDAO.updatePhotoDetails(photo);
		updatePhotoListInSession(session, userNo);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			if (result != false) {

				response.getWriter().write("{\"success\":true}");
			} else {
				response.getWriter().write("{\"success\":false}");
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// 선택한 사진 여러개 삭제 메서드
	public void deleteSelectedPhotos(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);
		if (userNo == null) {
			response.sendRedirect(request.getContextPath() + "/ErrorFile/sessionError.jsp");
			return;
		}

		String[] photoNoStrings = request.getParameterValues("photoNos");
		System.out.println("photoNoStrings: " + Arrays.toString(photoNoStrings)); // 디버깅 로그 추가
		if (photoNoStrings == null || photoNoStrings.length == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No photos selected for deletion.");
			return;
		}

		List<Integer> photoNos = Arrays.stream(photoNoStrings).filter(s -> s != null && !s.trim().isEmpty())
				.map(Integer::parseInt).collect(Collectors.toList());

		if (photoNos.isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No valid photos selected for deletion.");
			return;
		}

		String uploadDir = request.getServletContext().getRealPath("") + File.separator + "UploadedPhotos";
		for (int photoNo : photoNos) {
			photoDAO.deletePhotoByPhotoNo(photoNo, uploadDir);
		}
		updatePhotoListInSession(session, userNo);

		response.setContentType("application/json");
		response.getWriter().write("{\"success\":true}");
	}

	// 사진 하나 삭제 메서드
	public void deletePhoto(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		HttpSession session = request.getSession(false);
		Integer userNo = getUserNoFromSession(session);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (userNo == null) {
			response.getWriter().write("{\"success\": false, \"message\": \"세션이 만료되었습니다.\"}");
			return;
		}

		String photoNoStr = request.getParameter("no");

		if (photoNoStr == null || photoNoStr.isEmpty()) {
			response.getWriter().write("{\"success\": false, \"message\": \"잘못된 요청입니다.\"}");
			return;
		}

		Integer photoNo;

		try {
			photoNo = Integer.valueOf(photoNoStr);
		} catch (NumberFormatException e) {
			response.getWriter().write("{\"success\": false, \"message\": \"잘못된 번호 형식입니다.\"}");
			return;
		}

		String uploadDir = request.getServletContext().getRealPath("") + File.separator + "UploadedPhotos";

		try {
			photoDAO.deletePhotoByPhotoNo(photoNo, uploadDir);

			// 삭제 후 사진 목록을 다시 불러와 세션에 저장
			updatePhotoListInSession(session, userNo);

			response.getWriter().write("{\"success\": true}");
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().write("{\"success\": false, \"message\": \"데이터베이스 오류가 발생했습니다.\"}");
		}
	}
}