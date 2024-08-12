package com.smile.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smile.www.dto.CommentDTO;
import com.smile.www.utils.DBUtil;

public class CommentDAO {
	// 댓글 추가
	public boolean addComment(CommentDTO comment) throws SQLException {
		try (Connection conn = DBUtil.getConnection()) {
			String sql = "INSERT INTO Comments (post_no, user_no, content, parent_no) VALUES (?, ?, ?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, comment.getPostNo());
				pstmt.setInt(2, comment.getUserNo());
				pstmt.setString(3, comment.getContent());
				if (comment.getParentNo() != null) {
					pstmt.setInt(4, comment.getParentNo());
				} else {
					pstmt.setNull(4, java.sql.Types.INTEGER);
				}
				int affectedRows = pstmt.executeUpdate();
				return affectedRows > 0;
			}
		}
	}

	// 전체 댓글 가져오기 - 대댓글 까지
	public List<CommentDTO> getCommentsByPost(int postNo) throws SQLException {
		Map<Integer, CommentDTO> commentMap = new HashMap<>();
		List<CommentDTO> comments = new ArrayList<>();

		try (Connection conn = DBUtil.getConnection()) {
			String sql = "SELECT c.*, u.profile_image_url, u.username FROM Comments c "
					+ "JOIN Users u ON c.user_no = u.no WHERE c.post_no = ? ORDER BY c.created_at ASC";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, postNo);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						CommentDTO comment = new CommentDTO();
						comment.setNo(rs.getInt("no"));
						comment.setPostNo(rs.getInt("post_no"));
						comment.setUserNo(rs.getInt("user_no"));
						comment.setContent(rs.getString("content"));
						comment.setParentNo(rs.getObject("parent_no", Integer.class));
						comment.setCreatedAt(rs.getString("created_at"));
						comment.setProfileUrl(rs.getString("profile_image_url"));
						comment.setUsername(rs.getString("username"));
						comment.setReplies(new ArrayList<>()); // Initialize the replies list

						if (comment.getParentNo() == null) {
							comments.add(comment);
						} else {
							CommentDTO parentComment = commentMap.get(comment.getParentNo());
							if (parentComment != null) {
								parentComment.getReplies().add(comment);
							}
						}

						commentMap.put(comment.getNo(), comment);
					}
				}
			}
		}
		return comments;
	}

	// 댓글 수정
	public boolean updateComment(CommentDTO comment) throws SQLException {
		try (Connection conn = DBUtil.getConnection()) {
			String sql = "UPDATE Comments SET content = ?, created_at = CURRENT_TIMESTAMP WHERE no = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, comment.getContent());
				pstmt.setInt(2, comment.getNo());
				return pstmt.executeUpdate() > 0;
			}
		}
	}

	// 댓글 삭제
	public boolean deleteComment(int commentNo) {
		String sql = "DELETE FROM Comments WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, commentNo);
			int affectedRows = pstmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 마이 페이지에서 사용자 댓글 가져오기
	public List<CommentDTO> selectCommentsByUser(int userNo) {
		List<CommentDTO> comments = new ArrayList<>();
		String query = "SELECT c.*, u.username AS commentAuthor, u.profile_image_url AS profileUrl "
				+ "FROM comments c " + "JOIN users u ON c.user_no = u.no " + "WHERE c.user_no = ?";

		try (Connection connection = DBUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userNo);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				CommentDTO comment = new CommentDTO();
				comment.setNo(resultSet.getInt("c.no"));
				comment.setPostNo(resultSet.getInt("c.post_no"));
				comment.setUserNo(resultSet.getInt("c.user_no"));
				comment.setContent(resultSet.getString("c.content"));
				comment.setCreatedAt(resultSet.getString("c.created_at"));
				comment.setUsername(resultSet.getString("commentAuthor"));
				comment.setProfileUrl(resultSet.getString("profileUrl"));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return comments;
	}
}
