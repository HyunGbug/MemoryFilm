package com.smile.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smile.www.dto.PostDTO;
import com.smile.www.utils.DBUtil;

public class PostDAO {

	// 게시글 작성
	public void addPost(PostDTO postDTO) throws SQLException {
		String sql = "INSERT INTO Posts (user_no, title, content) VALUES (?, ?, ?)";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, postDTO.getUserNo());
			pstmt.setString(2, postDTO.getTitle());
			pstmt.setString(3, postDTO.getContent());
			pstmt.executeUpdate();

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int postNo = generatedKeys.getInt(1);
					postDTO.setNo(postNo); // PostDTO에 postNo 설정
				}
			}
		}
	}

	// 게시물 작성 사진 업로드
	public void addPostPhotos(int postNo, List<String> photoUrls) throws SQLException {
		String sql = "INSERT INTO PostPhotos (post_no, photo_url) VALUES (?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (String photoUrl : photoUrls) {
				pstmt.setInt(1, postNo);
				pstmt.setString(2, photoUrl);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
	}

	// 모든 게시글 불러오기 - 날짜 최근 순으로
	public List<PostDTO> getAllPosts() throws SQLException {
		String sql = "SELECT P.*, U.profile_image_url, U.username FROM Posts P JOIN Users U ON P.user_no = U.no ORDER BY P.created_at DESC ";
		List<PostDTO> posts = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			while (rs.next()) {
				PostDTO post = new PostDTO();
				post.setNo(rs.getInt("no"));
				post.setUserNo(rs.getInt("user_no"));
				post.setUsername(rs.getString("username"));
				post.setProfileUrl(rs.getString("profile_image_url"));
				post.setTitle(rs.getString("title"));
				post.setContent(rs.getString("content"));
				post.setHit(rs.getInt("hit"));
				post.setCreatedAt(rs.getString("created_at"));
				post.setUpdatedAt(rs.getString("updated_at"));
				post.setPhotoUrls(getPhotoUrlsByPostNo(post.getNo()));

				// 게시물에 연결된 사진 불러오기
				posts.add(post);
			}
		}
		return posts;
	}

	// 게시글에 연결된 사진 경로 불러오기 메서드
	public List<String> getPhotoUrlsByPostNo(int postNo) throws SQLException {
		String query = "SELECT photo_url FROM PostPhotos WHERE post_no = ?";
		List<String> photoUrls = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					photoUrls.add(rs.getString("photo_url"));
				}
			}
		}
		return photoUrls;
	}

	// post-no로 특정 게시글 불러오기
	public PostDTO getPostById(int postNo) {
		String query = "SELECT P.*, U.username, U.profile_image_url " + "FROM Posts P "
				+ "JOIN Users U ON P.user_no = U.no " + "WHERE P.no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setInt(1, postNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					PostDTO post = new PostDTO();
					post.setNo(rs.getInt("no"));
					post.setUserNo(rs.getInt("user_no"));
					post.setTitle(rs.getString("title"));
					post.setContent(rs.getString("content"));
					post.setHit(rs.getInt("hit"));
					post.setCreatedAt(rs.getString("created_at"));
					post.setUpdatedAt(rs.getString("updated_at"));
					post.setProfileUrl(rs.getString("profile_image_url"));
					post.setUsername(rs.getString("username"));
					post.setPhotoUrls(getPhotoUrlsByPostNo(postNo));
					return post;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 게시글 수정
	public void updatePost(PostDTO post) throws SQLException {
		String sql = "UPDATE Posts SET title = ?, content = ?, updated_at = CURRENT_TIMESTAMP WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, post.getTitle());
			pstmt.setString(2, post.getContent());
			pstmt.setInt(3, post.getNo());
			pstmt.executeUpdate();
		}
	}

	// 게시글 수정에 사진 삭제
	public void deletePostPhoto(int postNo, String photoUrl) throws SQLException {
		String sql = "DELETE FROM PostPhotos WHERE post_no = ? AND photo_url = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.setString(2, photoUrl);
			pstmt.executeUpdate();
		}
	}

	// 게시글 삭제
	public void deletePost(int postNo) throws SQLException {
		String sql = "DELETE FROM Posts WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.executeUpdate();
		}
	}

	// 사용자가 작성한 게시글 불러오기 - 마이페이지 용
	public List<PostDTO> getPostsByUser(int userNo) throws SQLException {
		String sql = "SELECT P.*, U.profile_image_url, U.username FROM Posts P JOIN Users U ON P.user_no = U.no WHERE P.user_no = ? ORDER BY P.created_at DESC";
		List<PostDTO> posts = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PostDTO post = new PostDTO();
					post.setNo(rs.getInt("no"));
					post.setUserNo(rs.getInt("user_no"));
					post.setUsername(rs.getString("username"));
					post.setProfileUrl(rs.getString("profile_image_url"));
					post.setTitle(rs.getString("title"));
					post.setContent(rs.getString("content"));
					post.setHit(rs.getInt("hit"));
					post.setCreatedAt(rs.getString("created_at"));
					post.setUpdatedAt(rs.getString("updated_at"));
					post.setPhotoUrls(getPhotoUrlsByPostNo(post.getNo()));
					posts.add(post);
				}
			}
		}
		return posts;
	}

	// 좋아요한 게시물의 전체 정보 반환 - 마이 페이지 용도
	public List<PostDTO> getLikedPostsByUser(int userNo) throws SQLException {
		String sql = "SELECT P.*, U.username, U.profile_image_url " + "FROM Posts P "
				+ "INNER JOIN Likes L ON P.no = L.post_no " + "INNER JOIN Users U ON P.user_no = U.no "
				+ "WHERE L.user_no = ? " + "ORDER BY P.created_at DESC";
		List<PostDTO> posts = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PostDTO post = new PostDTO();
					post.setNo(rs.getInt("no"));
					post.setUserNo(rs.getInt("user_no"));
					post.setUsername(rs.getString("username"));
					post.setProfileUrl(rs.getString("profile_image_url"));
					post.setTitle(rs.getString("title"));
					post.setContent(rs.getString("content"));
					post.setHit(rs.getInt("hit"));
					post.setCreatedAt(rs.getString("created_at"));
					post.setUpdatedAt(rs.getString("updated_at"));
					post.setPhotoUrls(getPhotoUrlsByPostNo(post.getNo()));
					posts.add(post);
				}
			}
		}
		return posts;
	}

	// 좋아요한 게시물 번호만 반환 - 커뮤니티 용도
	public List<Integer> getLikedPostNosByUser(int userNo) throws SQLException {
		String sql = "SELECT post_no FROM Likes WHERE user_no = ?";
		List<Integer> likedPostNos = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					likedPostNos.add(rs.getInt("post_no"));
				}
			}
		}
		return likedPostNos;
	}

	// 조회수 업데이트
	public void increaseHit(int postNo) throws SQLException {
		String sql = "UPDATE Posts SET hit = hit + 1 WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.executeUpdate();
		}
	}

	// 좋아요 상태 확인
	public boolean isPostLikedByUser(int postNo, int userNo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Likes WHERE post_no = ? AND user_no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.setInt(2, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 좋아요 추가
	public void addLike(int postNo, int userNo) throws SQLException {
		String sql = "INSERT INTO Likes (post_no, user_no) VALUES (?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.setInt(2, userNo);
			pstmt.executeUpdate();
		}
	}

	// 좋아요 취소
	public void removeLike(int postNo, int userNo) throws SQLException {
		String sql = "DELETE FROM Likes WHERE post_no = ? AND user_no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, postNo);
			pstmt.setInt(2, userNo);
			pstmt.executeUpdate();
		}
	}

}