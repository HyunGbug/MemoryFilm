package com.smile.www.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.smile.www.dao.PhotoDAO;
import com.smile.www.dao.UserDAO;
import com.smile.www.dto.UserDTO;
import com.smile.www.utils.EmailUtil;

public class UserService {
	private UserDAO userDAO;
	private PhotoDAO photoDAO;
	private PhotoService photoService;
	public UserService() {
		this.userDAO = new UserDAO();
		this.photoDAO = new PhotoDAO();
		this.photoService = new PhotoService();
	}

	// 회원가입 시 아이디 중복 체크
	public void checkId(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String id = request.getParameter("id");
		boolean isDuplicate = userDAO.isIdDuplicate(id);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}

	// 회원가입 시 닉네임 중복 체크
	public void checkUsername(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String username = request.getParameter("username");
		boolean isDuplicate = userDAO.isUsernameDuplicate(username);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}

	// 회원가입 시 이메일 중복 체크
	public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String email = request.getParameter("email");
		boolean isDuplicate = userDAO.isEmailDuplicate(email);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}

	// 회원 정보 수정 시 아이디 중복 체크(본인 아이디는 예외 처리)
	public void checkUpdateId(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String id = request.getParameter("id");
		int currentUserNo = (int) request.getSession().getAttribute("userNo");
		boolean isDuplicate = userDAO.idDuplicateForUpdate(id, currentUserNo);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}

	// 회원 정보 수정 시 닉네임 중복 체크
	public void checkUpdateUsername(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String username = request.getParameter("username");
		int currentUserNo = (int) request.getSession().getAttribute("userNo");
		boolean isDuplicate = userDAO.usernameDuplicateForUpdate(username, currentUserNo);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}

	// 회원 정보 수정 시 이메일 중복 체크
	public void checkUpdateEmail(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		String email = request.getParameter("email");
		int currentUserNo = (int) request.getSession().getAttribute("userNo"); // 세션에서 현재 사용자 번호를 가져옴
		boolean isDuplicate = userDAO.emailDuplicateForUpdate(email, currentUserNo);
		sendJsonResponse(response, "{\"isDuplicate\": " + isDuplicate + "}");
	}
	
	// 회원 가입
	public void signup(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		UserDTO user = new UserDTO();
		user.setUserId(request.getParameter("id"));
		user.setUserName(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		user.setProfileUrl(saveProfileImage(request, true)); // 프로필 사진 저장 및 URL 생성
		user.setMemo("");

		boolean success = userDAO.registerUser(user);

		if (success) {
			sendJsonResponse(response, "{\"success\": true, \"message\": \"회원가입이 완료되었습니다!\"}");
		} else {
			sendJsonResponse(response, "{\"success\": false, \"message\": \"회원가입에 실패했습니다. 다시 시도해주세요.\"}");
		}

	}
	
	//사용자가 아이디를 잊었을 때
	//아이디 알려주기
	public void findUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		try {
			String userId = userDAO.findUserIdByEmail(email);
			if (userId == null) {
				 request.setAttribute("error", "해당 이메일로 가입된 계정이 없습니다.");
		            RequestDispatcher dispatcher = request.getRequestDispatcher("/Html/FindUserAccount/Find_Id.jsp");
		            dispatcher.forward(request, response);
		            return;
			}
			request.setAttribute("userId", userId);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Html/FindUserAccount/FoundId.jsp"); //절대 경로로 바꾸기
			dispatcher.forward(request, response);
		}catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "데이터베이스 오류가 발생했습니다.");
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Html/ErrorFile/Error.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			request.setAttribute("error", e.getMessage());
			RequestDispatcher dispatcher = request.getRequestDispatcher("/Html/ErrorFile/Error.jsp");
			dispatcher.forward(request, response);
		}
	}

	// 사용자가 비밀번호를 잊었을 때
	// 비밀번호 재설정
	public void resetPassword(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String userId = request.getParameter("id");
		String email = request.getParameter("email");

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try {
			if (userDAO.isUserValid(userId, email)) {
				String tempPassword = generateTempPassword();
				EmailUtil.sendEmail(email, "Memory Film :: 임시 비밀번호 보내드립니다.",
						"변경된 임시 비밀번호 : " + tempPassword + "<br><br> 로그인 후 반드시 MyPage에서 비밀번호를 재설정 해주시기 바랍니다.");
				boolean updateSuccess = userDAO.updatePassword(userId, tempPassword);

				if (updateSuccess) {
					response.getWriter().write("{\"success\": true, \"message\": \"임시 비밀번호를 이메일로 전송했습니다.\"}");
				} else {
					response.getWriter().write("{\"success\": false, \"message\": \"임시 비밀번호 업데이트 실패.\"}");
				}
			} else {
				response.getWriter().write("{\"success\": false, \"message\": \"해당 계정이 존재하지 않습니다. 아이디와 이메일을 확인해주세요.\"}");
			}
		} catch (MessagingException e) {
			response.getWriter().write("{\"success\": false, \"message\": \"메일 전송 실패\"}");
			e.printStackTrace();
		} catch (Exception e) {
			response.getWriter().write("{\"success\": false, \"message\": \"서버 오류가 발생했습니다.\"}");
			e.printStackTrace();
		}
	}

	// 임시 비밀번호 생성
	private String generateTempPassword() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$!%*?&";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(10);
		for (int i = 0; i < 10; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	// 사용자가 로그인 했을 때 데이터베이스에서 비교하고, 정보가져와 세션 및 쿠키 생성
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		String userId = request.getParameter("id");// 아이디
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		int userNo = userDAO.loginUser(userId, password);// 존재하는 사용자의 no값 받아오기

		if (userNo > 0) { // != -1 도 가능
			UserDTO user = userDAO.getUserByNo(userNo); // no이용해서 로그인한 사용자 정보 가져오기
			HttpSession session = request.getSession();
			session.setAttribute("user", user); // 가져온 정보를 세션에 저장
			session.setAttribute("userNo", userNo); // userNo를 세션에 저장

			// 사진 목록을 불러와 세션에 저장
			photoService.updatePhotoListInSession(session, userNo);

			// Remember Me 기능 구현
			if ("on".equals(rememberMe)) {
				addRememberMeCookie(response, userId);

			} else {
				removeRememberMeCookie(response);
			}

			// RequestDispatcher를 사용하여 viewPage로 포워드
			response.sendRedirect(request.getContextPath() + "/Html/HomePage(Login)_UI.jsp");
		} else {
			response.sendRedirect(request.getContextPath() + "/Html/LoginForm_UI.jsp?error=true");
		}
	}

	// 메모 업데이트 메서드 추가
	public void updateMemo(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		HttpSession session = request.getSession(false);

		// null 체크 추가
		if (session == null || session.getAttribute("userNo") == null) {
			// 세션이 없거나 userNo 속성이 없을 경우 적절한 오류 처리를 수행합니다.
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session not found");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		int userNo = (int) session.getAttribute("userNo");
		String memo = request.getParameter("note");

		userDAO.updateUserMemo(userNo, memo);

		UserDTO updatedUser = userDAO.getUserByNo(userNo);
		session.setAttribute("user", updatedUser); // 업데이트된 사용자 정보를 세션에 다시 저장

		response.setContentType("application/json");
		try {
			response.getWriter().write("{\"success\":true}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 사용자가 본인 정보 수정했을 때
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {

		HttpSession session = request.getSession(false); // 요청에 기존 세션이 있는 경우 해당 세션을 반환하고, 없으면 null 빈환

		if (session == null || session.getAttribute("userNo") == null) {
			sendJsonResponse(response, "{\"success\": false, \"message\": \"세션이 만료되었습니다. 다시 로그인 해주세요.\"}");
			return;
		}

		int userNo = (int) session.getAttribute("userNo");// 수정하는 계정 번호 가져오기

		UserDTO user = userDAO.getUserByNo(userNo);// 현재 사용자 정보를 데이터베이스에서 조회

		// 사용자 정보 업데이트
		user.setUserId(request.getParameter("id"));
		user.setUserName(request.getParameter("username"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));

		String newProfileUrl = saveProfileImage(request, false); // 프로필 사진 업데이트
		if (newProfileUrl != null) {
			user.setProfileUrl(newProfileUrl);
		}

		boolean updateSuccess = userDAO.updateUser(user);// 변경된 정보를 데이터베이스에 반영

		// 변경된 사용자 정보 다시 조회
		UserDTO updatedUser = userDAO.getUserByNo(userNo);
		// 세션에 업데이트된 사용자 정보 저장
		session.setAttribute("user", updatedUser);

		if (updateSuccess) {
			sendJsonResponse(response, "{\"success\": true, \"message\": \"수정 완료!\"}");
		} else {
			sendJsonResponse(response, "{\"success\": false, \"message\": \"수정 실패!\"}");
		}
	}

	// 프로필 이미지 저장
	private String saveProfileImage(HttpServletRequest request, boolean isSignup) throws IOException, ServletException {
		Part filePart = request.getPart("profilePicture");

		if (filePart == null || filePart.getSize() == 0) {
			// 회원가입 시 기본 프로필 사진을 설정
			if (isSignup) {
				return request.getContextPath() + "/DefaultProfile/defaultProfile.png"; // 기본 이미지 경로 설정
			} else {
				return null; // 본인 수정 떄 프로필 사진 변경이면 null 반환
			}
		}

		String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // 클라이언트가 업로드한 파일의 원래 이름
																								// 반환
		// 절대 경로 사용해 서버에 파일 저장
		String uploadDir = request.getServletContext().getRealPath("") + File.separator + "ProfileImages"; // 실제 서버에
																											// 업로드되는 경로

		// 업로드 디렉토리 존재 여부 확인 및 생성
		File uploadDirFile = new File(uploadDir);
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}

		// 파일 이름 중복 방지를 위해 고유한 파일 이름 생성
		String uniqueFileName = generateUniqueFileName(uploadDir, fileName);
		File file = new File(uploadDir, uniqueFileName);

		try (InputStream input = filePart.getInputStream()) {
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("파일 저장 완료: " + file.getAbsolutePath()); // 로그 추가
		} catch (IOException e) {
			e.printStackTrace();
			throw new IOException("파일 저장 실패: " + e.getMessage());
		}

		// URL 인코딩
		String encodedFileName = URLEncoder.encode(uniqueFileName, "UTF-8").replace("+", "%20");
		// 상대 경로로 저장된 파일의 URL을 생성하여 반환
		return request.getContextPath() + "/ProfileImages/" + encodedFileName;
	}

	// 파일 이름 생성하는 메서드
	private String generateUniqueFileName(String uploadDir, String fileName) {
		String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
		String extension = fileName.substring(fileName.lastIndexOf('.'));
		// 날짜 포맷 설정
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());

		// 날짜를 파일 이름에 추가 (언더스코어 사용)
		String uniqueFileName = baseName + "_업로드날짜_" + date + extension;
		File file = new File(uploadDir, uniqueFileName);
		int count = 1;
		while (file.exists()) {
			uniqueFileName = baseName + "_업로드날짜_" + date + "(" + count + ")" + extension;
			file = new File(uploadDir, uniqueFileName);
			count++;
		}
		return uniqueFileName;
	}

	// 로그아웃 - 포워딩으로
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}

		// 캐시 무효화 헤더 설정
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		System.out.println("로그아웃 완료 - 세션 삭제, 캐시 무효화");
		response.sendRedirect(request.getContextPath() + "/HomePage(LoginX)_UI.jsp");
	}

	// 사용자 계정 삭제
	public String delete(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		HttpSession session = request.getSession();
		UserDTO user = (UserDTO) session.getAttribute("user");

		if (user != null) {
			String inputPassword = request.getParameter("password");
			if (inputPassword == null || inputPassword.isEmpty()) {
				response.setContentType("application/json");
				response.getWriter().write("{\"success\": false, \"message\": \"비밀번호를 입력해주세요.\"}");
				return null;
			}

			String dbPassword = userDAO.getPasswordByUserNo(user.getNo());

			if (inputPassword.equals(dbPassword)) {

				// 사용자 사진 파일 삭제
				String uploadDir = request.getServletContext().getRealPath("") + File.separator + "UploadedPhotos";
				photoDAO.deletePhotoRecordsByUserNo(user.getNo(), uploadDir);

				// 사용자 계정 삭제
				userDAO.deleteUser(user.getNo());
				session.invalidate();
				// Remember Me 쿠키 삭제
				removeRememberMeCookie(response);
				sendJsonResponse(response, "{\"success\": true, \"message\": \"계정이 성공적으로 삭제되었습니다.\"}");
			} else {
				sendJsonResponse(response, "{\"success\": false, \"message\": \"비밀번호가 올바르지 않습니다.\"}");
			}
		}
		return null;
	}

	// 공통 응답 메서드
	private void sendJsonResponse(HttpServletResponse response, String jsonResponse) throws IOException {
		response.setContentType("application/json");
		response.getWriter().write(jsonResponse);
	}

	// Remember Me 쿠키 설정 메서드
	private void addRememberMeCookie(HttpServletResponse response, String userId) {
		Cookie cookie = new Cookie("rememberUserId", userId);// 아이디 기억하기 버튼에 체크 되어있으면 쿠키에 사용자 아이디 저장
		cookie.setMaxAge(60 * 60 * 24 * 7); // 7일
		cookie.setPath("/"); // 루트 경로로 설정
		response.addCookie(cookie);
	}

	// Remember Me 쿠키 삭제 메서드
	private void removeRememberMeCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("rememberUserId", "");
		cookie.setMaxAge(0); // 쿠키 삭제
		cookie.setPath("/"); // 루트 경로로 설정
		response.addCookie(cookie);
	}

}
