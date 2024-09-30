package com.smile.www.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import com.smile.www.dto.UserDTO;
import com.smile.www.utils.DBUtil;

public class UserDAO {

	// 회원 가입 아이디 중복 확인
	public boolean isIdDuplicate(String id) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 회원 가입 닉네임 중복 확인
	public boolean isUsernameDuplicate(String username) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 회원 가입 이메일 중복 확인
	public boolean isEmailDuplicate(String email) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE email = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 회원 정보 수정 시 아이디 중복 확인
	public boolean idDuplicateForUpdate(String id, int currentUserNo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE user_id = ? AND no != ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, id);
			ps.setInt(2, currentUserNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 회원 정보 수정 시 닉네임 중복 확인
	public boolean usernameDuplicateForUpdate(String username, int currentUserNo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE username = ? AND no != ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setInt(2, currentUserNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 회원 정보 수정 시 이메일 중복 확인
	public boolean emailDuplicateForUpdate(String email, int currentUserNo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Users WHERE email = ? AND no != ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setInt(2, currentUserNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	// 사용자 정보를 데이터베이스에 저장하는 메서드 - 회원가입 성공
	public boolean registerUser(UserDTO user) throws SQLException {
		String sql = "INSERT INTO Users (user_id, username, email, password, profile_image_url, memo, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getUserName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getProfileUrl()); // 인코딩된 URL 저장
			ps.setString(6, user.getMemo());
			ps.setTimestamp(7, user.getCreatedAt());
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLIntegrityConstraintViolationException e) {
			// 이 부분을 추가하여 예외를 캐치하고 사용자에게 알림
			System.err.println("중복된 ID로 인해 회원가입 실패: " + e.getMessage());
			return false;
		}
	}
	
	//email로 id찾기
	public String findUserIdByEmail(String email) throws SQLException{
		String sql = "select user_id from users where email = ?";
		try(Connection conn = DBUtil.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, email);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return rs.getString("user_id");
				}
			}
		}
		return null;
	}
	
	//아이디와 이메일 이용해서 해당 계정이 존재하는지
	public boolean isUserValid(String userId, String email) throws SQLException {
	    String query = "SELECT COUNT(*) FROM Users WHERE user_id = ? AND email = ?";
	    try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, userId);
	        pstmt.setString(2, email);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	//이메일로 임시비밀번호 보내서 해당 임시 비밀번호를 데이터베이스에 업데이트하기
	public boolean updatePassword(String userId, String newPassword) throws SQLException {
	    String query = "UPDATE Users SET password = ? WHERE user_id = ?";
	    try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userId);
	        return pstmt.executeUpdate() > 0;
	    }
	}

	// 로그인 메서드 - ID와 비밀번호로 사용자 no 조회
	public int loginUser(String userId, String password) throws SQLException {
		String sql = "SELECT no FROM Users WHERE user_id = ? AND password = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, userId);
			ps.setString(2, password);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("no");
				}
			}
		}
		return -1; // 해당 사용자가 존재하지 않으면 -1 반환
	}

	// 주 키를 이용해 사용자 정보 조회 > 로그인 된 상태에서 세션에 저장된 번호 이용해 사용자 정보 가져옴
	// 사용자 no로 사용자 정보 조회 메서드
	public UserDTO getUserByNo(int userNo) throws SQLException {
		String sql = "SELECT * FROM Users WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					UserDTO user = new UserDTO();
					user.setNo(rs.getInt("no"));
					user.setUserId(rs.getString("user_id"));
					user.setUserName(rs.getString("username"));
					user.setEmail(rs.getString("email"));
					user.setProfileUrl(rs.getString("profile_image_url")); // 디코딩된 URL 설정
					user.setMemo(rs.getString("memo"));
					user.setCreatedAt(rs.getTimestamp("created_at"));
					user.setRole(rs.getString("role")); // 역할 설정
					return user;
				}
			}
		}
		return null;
	}

	// 홈에서 메모 업데이트
	public void updateUserMemo(int userNo, String memo) throws SQLException {
		String sql = "UPDATE Users SET memo = ? WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, memo);
			pstmt.setInt(2, userNo);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 사용자가 본인 정보 수정하면 db에 업데이트
	public boolean updateUser(UserDTO user) throws SQLException {
		String sql = "UPDATE Users SET user_id = ?, username = ?, email = ?, password = ?, profile_image_url = ? WHERE no = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, user.getUserId());
			ps.setString(2, user.getUserName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getProfileUrl());
			ps.setInt(6, user.getNo());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 탈퇴 전 비밀번호 검증을 위한 메서드 해당 사용자 비밀번호 가져오기
	public String getPasswordByUserNo(int userNo) throws SQLException {
		String sql = "SELECT password FROM Users WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userNo);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("password");
				}
			}
		}
		return null;
	}

	// 회원 탈퇴
	public void deleteUser(int userNo) throws SQLException {
		String sql = "DELETE FROM Users WHERE no = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userNo);
			ps.executeUpdate();
		}
	}

	// 관리자인지 사용자인지 구분
	public boolean isAdmin(String userId) throws SQLException {
		String sql = "SELECT * FROM Admins WHERE admin_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
}
