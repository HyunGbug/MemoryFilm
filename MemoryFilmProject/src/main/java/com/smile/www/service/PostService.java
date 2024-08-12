package com.smile.www.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.smile.www.dao.PhotoDAO;
import com.smile.www.dao.PostDAO;
import com.smile.www.dto.PostDTO;
import com.smile.www.dto.UserDTO;

public class PostService {
	private PostDAO postDAO;
	private PhotoDAO photoDAO;

	public PostService() {
		this.postDAO = new PostDAO();
		this.photoDAO = new PhotoDAO();
	}

	// 세션, 속성 검증하기
	private Integer getUserNoFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userNo") == null) {
			return null;
		}
		return (Integer) session.getAttribute("userNo");

	}

	// 게시물 정보 가져오기
	public PostDTO getPostById(int postNo) throws SQLException {
		return postDAO.getPostById(postNo);
	}

	// 게시물 글쓰기
	public boolean addPost(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		Integer userNo = getUserNoFromSession(request);
		if (userNo == null) {
			request.setAttribute("error", "세션이 만료되었습니다.");
			return false;
		}

		String title = request.getParameter("title");
		String content = request.getParameter("content");

		PostDTO post = new PostDTO();
		post.setUserNo(userNo);
		post.setTitle(title);
		post.setContent(content);

		postDAO.addPost(post);// 먼저 게시물을 추가하고

		List<Integer> photoNos = new ArrayList<>();
		List<String> photoUrls = new ArrayList<>();
		savePostPhotos(request, photoNos, photoUrls, userNo);
		post.setPhotoNos(photoNos);

		postDAO.addPostPhotos(post.getNo(), photoUrls);// 사진 정보를 추가
		return true;
	}

	// 게시판에 글 쓸 때 이미지 서버에 등록
	private void savePostPhotos(HttpServletRequest request, List<Integer> photoNos, List<String> photoUrls,
			Integer userNo) throws IOException, ServletException, SQLException {
		List<Part> fileParts = (List<Part>) request.getParts();
		String uploadDir = request.getServletContext().getRealPath("") + File.separator + "PostPhotos";

		// PostPhotos 폴더가 없으면 생성
		File uploadDirFile = new File(uploadDir);
		if (!uploadDirFile.exists()) {
			uploadDirFile.mkdirs();
		}

		// 새로 업로드 하는 파일 처리
		for (Part part : fileParts) {
			if (part.getName().equals("newPhotos") && part.getSize() > 0) {
				String originalFileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
				String uniqueFileName = generateUniqueFileName(uploadDir, originalFileName, userNo);
				String filePath = "PostPhotos/" + uniqueFileName;
				part.write(uploadDir + File.separator + uniqueFileName);
				// 새로 업로드된 파일의 경로를 photoUrls 리스트에 추가
				photoUrls.add(filePath);
			}
		}

		// 이미 업로드된 사진 처리 - PostPhotos 폴더에 복사해서 해당 경로로 저장
		String existingPhotoNosParam = request.getParameter("selectedExistingPhotos");
		if (existingPhotoNosParam != null && !existingPhotoNosParam.trim().isEmpty()) {
			String[] existingPhotoNos = existingPhotoNosParam.split(",");
			for (String photoNoStr : existingPhotoNos) {
				if (photoNoStr != null && !photoNoStr.trim().isEmpty()) {
					try {
						int photoNo = Integer.parseInt(photoNoStr.trim());
						String existingPhotoUrl = photoDAO.getPhotoUrlByPhotoNo(photoNo);
						if (existingPhotoUrl != null) {
							// 기존 사진을 PostPhotos 폴더에 복사
							String uniqueFileName = generateUniqueFileName(uploadDir,
									Paths.get(existingPhotoUrl).getFileName().toString(), userNo);
							File sourceFile = new File(
									request.getServletContext().getRealPath("") + File.separator + existingPhotoUrl);
							File destFile = new File(uploadDir + File.separator + uniqueFileName);
							Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							String copiedFilePath = "PostPhotos/" + uniqueFileName;

							photoUrls.add(copiedFilePath); // 복사된 파일의 경로를 photoUrls 리스트에 저장합니다.
						}
					} catch (NumberFormatException | IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
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

	// 업로드된 전체 게시물 불러오기
	public void listPosts(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		List<PostDTO> posts = postDAO.getAllPosts();
		request.setAttribute("posts", posts);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/Html/Community.jsp");
		dispatcher.forward(request, response);
	}

	// 해당 게시물 자세히 보기 - 조회수 증가와, 세션 검증을 통해 게시물 수정 및 삭제
	public PostDTO viewPost(HttpServletRequest request) throws SQLException {

		HttpSession session = request.getSession();
		String postNoParam = request.getParameter("postNo");
		if (postNoParam == null || postNoParam.isEmpty()) {
			throw new IllegalArgumentException("게시글 번호가 필요합니다.");
		}

		int postNo = Integer.parseInt(postNoParam);

		Map<Integer, Long> viewedPosts = getViewedPostsFromSession(session);

		long currentTime = System.currentTimeMillis();
		long viewInterval = 60 * 1000; // 1분 (밀리초 단위)로 설정

		boolean isFirstView = false;

		if (!viewedPosts.containsKey(postNo) || (currentTime - viewedPosts.get(postNo)) > viewInterval) {
			viewedPosts.put(postNo, currentTime);
			session.setAttribute("viewedPosts", viewedPosts); // 세션에 업데이트된 viewedPosts를 저장
			isFirstView = true;
		}

		if (isFirstView) {
			postDAO.increaseHit(postNo); // 조회수 증가

		}

		return getPostById(postNo); // 해당 게시물 정보 가져오기

	}

	@SuppressWarnings("unchecked")
	private Map<Integer, Long> getViewedPostsFromSession(HttpSession session) {
		Object viewedPostsObj = session.getAttribute("viewedPosts");
		if (viewedPostsObj instanceof Map<?, ?>) {
			return (Map<Integer, Long>) viewedPostsObj;
		} else {
			return new HashMap<>();
		}
	}

	// 본인 게시물 수정하기
	public int updatePost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		String title = request.getParameter("title");
		String content = request.getParameter("content");

		PostDTO post = new PostDTO();
		post.setNo(postNo);
		post.setTitle(title);
		post.setContent(content);

		// 게시글 업데이트
		postDAO.updatePost(post);

		// 사진 삭제 처리
		String[] photoUrlsToDelete = request.getParameterValues("deletePhotos");
		if (photoUrlsToDelete != null) {
			String uploadDir = request.getServletContext().getRealPath("");
			for (String photoUrl : photoUrlsToDelete) {
				File file = new File(uploadDir, photoUrl);
				System.out.println("파일 업데이트 삭제: " + file.getAbsolutePath());
				if (file.exists()) {
					file.delete();
				}
				// 데이터베이스에서도 삭제
				postDAO.deletePostPhoto(postNo, photoUrl);
			}
		}

		return postNo;
	}

	// 게시물 삭제 메서드
	public void deletePost(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		Integer userNo = getUserNoFromSession(request);
		if (userNo == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int postNo = Integer.parseInt(request.getParameter("postNo"));
		HttpSession session = request.getSession();
		PostDTO post = getPostById(postNo);
		UserDTO currentUser = (UserDTO) session.getAttribute("user");
		if (post != null && post.getUsername().equals(currentUser.getUserName())) {

			List<String> photoUrls = postDAO.getPhotoUrlsByPostNo(postNo);
			String uploadDir = request.getServletContext().getRealPath("");
			for (String photoUrl : photoUrls) {

				File file = new File(uploadDir, photoUrl);
				if (file.exists()) {
					boolean deleted = file.delete();
					if (deleted) {
						System.out.println("파일 삭제 성공: " + file.getAbsolutePath());
					} else {
						System.out.println("파일 삭제 실패: " + file.getAbsolutePath());
					}
				} else {
					System.out.println("파일이 존재하지 않음: " + file.getAbsolutePath());
				}
			}
			// 게시글 삭제
			postDAO.deletePost(postNo);
		}
	}

	// 본인이 작성한 게시글 가져오는 메서드 - 마이페이지용
	public List<PostDTO> getPostsByUser(int userNo) throws SQLException {
		return postDAO.getPostsByUser(userNo);
	}
	
	 // 좋아요한 게시물 번호만 반환하는 메서드
    private List<Integer> getLikedPostNosByUser(int userNo) throws SQLException {
        return postDAO.getLikedPostNosByUser(userNo);
    }

	// 데이터베이스에서 좋아요한 게시물 목록을 가져오는 메서드 - 마이페이지용, 커뮤니티용
	public List<PostDTO> getLikedPostsByUser(int userNo) throws SQLException {
		return postDAO.getLikedPostsByUser(userNo);
	}

	// HTTP 요청을 처리하여 게시글의 좋아요 상태를 토글하는 메서드
	public void toggleLike(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {

		Integer userNo = getUserNoFromSession(request);
		if (userNo == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		int postNo = Integer.parseInt(request.getParameter("postNo"));
		boolean isLiked = postDAO.isPostLikedByUser(postNo, userNo);

		if (isLiked) {
			postDAO.removeLike(postNo, userNo);
		} else {
			postDAO.addLike(postNo, userNo);
		}

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");

		String jsonResponse = "{\"liked\": " + !isLiked + "}";

		response.getWriter().write(jsonResponse);
	}

	// HTTP 요청을 처리하여 사용자에게 좋아요한 게시물 목록을 반환하는 메서드
	public void getLikedPostStatus(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {

		Integer userNo = getUserNoFromSession(request);
		if (userNo == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		 List<Integer> likedPosts = getLikedPostNosByUser(userNo);

		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType("application/json");

		StringJoiner jsonArray = new StringJoiner(",", "[", "]");
		for (Integer postNo : likedPosts) {
			jsonArray.add(postNo.toString());
		}

		response.getWriter().write(jsonArray.toString());
	}
}
