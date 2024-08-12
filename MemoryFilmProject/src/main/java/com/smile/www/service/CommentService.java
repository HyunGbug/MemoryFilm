package com.smile.www.service;

import java.sql.SQLException;
import java.util.List;

import com.smile.www.dao.CommentDAO;
import com.smile.www.dto.CommentDTO;

public class CommentService {
	private CommentDAO commentDAO;

	public CommentService() {
		commentDAO = new CommentDAO();
	}

	public boolean addComment(int postNo, int userNo, String content, Integer parentNo) throws SQLException {
		// 비즈니스 로직 수행 (예: 유효성 검사, 권한 체크 등)
		if (content == null || content.isEmpty()) {
			System.out.println("Content is empty"); // 로그 추가
			return false;
		}

		CommentDTO comment = new CommentDTO();
		comment.setPostNo(postNo);
		comment.setUserNo(userNo);
		comment.setContent(content);
		comment.setParentNo(parentNo);

		return commentDAO.addComment(comment);
	}

	public List<CommentDTO> getCommentsByPost(int postNo) throws SQLException {
		return commentDAO.getCommentsByPost(postNo);
	}

	public boolean updateComment(int commentNo, String content) throws SQLException {
		CommentDTO comment = new CommentDTO();
		comment.setNo(commentNo);
		comment.setContent(content);
		return commentDAO.updateComment(comment);
	}

	public boolean deleteComment(int commentNo) {
		return commentDAO.deleteComment(commentNo);
	}

	public List<CommentDTO> getCommentsByUser(int userNo) {
		return commentDAO.selectCommentsByUser(userNo);
	}

}
