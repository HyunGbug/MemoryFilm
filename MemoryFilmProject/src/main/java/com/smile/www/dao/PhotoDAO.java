package com.smile.www.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.smile.www.dto.PhotoDTO;
import com.smile.www.utils.DBUtil;

public class PhotoDAO {

	// 특정 photoNo로부터 사진 URL을 가져오는 메서드 - 게시판 용도
	public String getPhotoUrlByPhotoNo(int photoNo) throws SQLException {
	    String sql = "SELECT file_path FROM Photos WHERE no = ?";
	    try (Connection conn = DBUtil.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, photoNo);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("file_path");
	            }
	        }
	    }
	    return null;
	}


	
	// 데이터베이스에 사진 정보 삽입
	public void addPhoto(PhotoDTO photo) throws SQLException {
		String sql = "INSERT INTO Photos (user_no, file_path) VALUES (?, ?)";

		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, photo.getUserNo());
			pstmt.setString(2, photo.getFilePath());
			pstmt.executeUpdate();

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					photo.setNo(generatedKeys.getInt(1));
				}
			}
		}
	}

	// 사용자별 사진 목록 가져오기
	public List<PhotoDTO> getPhotosByUser(int userNo) throws SQLException {
		String sql = "SELECT * FROM Photos WHERE user_no = ? ORDER BY upload_date DESC";
		List<PhotoDTO> photos = new ArrayList<>();

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					PhotoDTO photo = new PhotoDTO();
					photo.setNo(rs.getInt("no"));
					photo.setUserNo(rs.getInt("user_no"));
					photo.setFilePath(rs.getString("file_path"));
					photo.setUploadDate(rs.getString("upload_date"));
					photos.add(photo);
				}
			}
		}
		return photos;
	}

	// 사용자별 업로드한 사진 개수 가져오기
	public int getPhotoCountByUser(int userNo) throws SQLException {
		String sql = "SELECT COUNT(*) FROM Photos WHERE user_no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return 0;
	}

	// 특정 사진 정보 가져오기
	public PhotoDTO getPhoto(int no) throws SQLException {
		String sql = "SELECT * FROM Photos WHERE no = ?";
		PhotoDTO photo = null;

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, no);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					photo = new PhotoDTO();
					photo.setNo(rs.getInt("no"));
					photo.setUserNo(rs.getInt("user_no"));
					photo.setFilePath(rs.getString("file_path"));
					photo.setUploadDate(rs.getString("upload_date"));
					photo.setCameraInfo(rs.getString("camera_info"));
					photo.setLensInfo(rs.getString("lens_info"));
					photo.setLocationInfo(rs.getString("location_info"));
					photo.setMemo(rs.getString("memo"));
				}
			}
		}
		return photo;
	}

	// 사진 정보 업데이트
	public boolean updatePhotoDetails(PhotoDTO photo) throws SQLException {
		String sql = "UPDATE Photos SET camera_info = ?, lens_info = ?, location_info = ?, memo = ? WHERE no = ?";

		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, photo.getCameraInfo());
			pstmt.setString(2, photo.getLensInfo());
			pstmt.setString(3, photo.getLocationInfo());
			pstmt.setString(4, photo.getMemo());
			pstmt.setInt(5, photo.getNo());
			int result = pstmt.executeUpdate();
			return result > 0;
		}
	}

	// 사용자별 모든 사진 경로 가져오기
	public List<String> getPhotoPathsByUserNo(int userNo) throws SQLException {
		String sql = "SELECT file_path FROM Photos WHERE user_no = ?";
		List<String> photoPaths = new ArrayList<>();
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, userNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					photoPaths.add(rs.getString("file_path"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return photoPaths;
	}

	// 사진 경로 가져오기
	public String getPhotoPathByPhotoNo(int photoNo) throws SQLException {
		String sql = "SELECT file_path FROM Photos WHERE no = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, photoNo);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getString("file_path");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return null;
	}

	// 특정 사진 삭제
	public void deletePhotoByPhotoNo(int photoNo, String uploadDir) throws SQLException {
		String sql = "SELECT file_path FROM Photos WHERE no = ?";
		String deleteSql = "DELETE FROM Photos WHERE no = ?";
		try (Connection conn = DBUtil.getConnection()) {
			// 파일 경로를 가져오기 위한 쿼리 실행
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, photoNo);
				try (ResultSet rs = pstmt.executeQuery()) {
					if (rs.next()) {
						String filePath = rs.getString("file_path");
						File file = new File(uploadDir, new File(filePath).getName());
						if (file.exists() && !file.isDirectory()) { // 폴더가 아닌 경우에만 삭제
							boolean deleted = file.delete();
							if (deleted) {
								System.out.println("Deleted file: " + file.getAbsolutePath());
							} else {
								System.out.println("Failed to delete file: " + file.getAbsolutePath());
							}
						}
					}
				}
			}
			// 사진 정보를 삭제하기 위한 쿼리 실행
			try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
				deletePstmt.setInt(1, photoNo);
				deletePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("데이터베이스 작업 중 오류 발생: " + e.getMessage());
			throw e; // 예외를 다시 던져서 호출자에게 알림
		}
	}

	// 사용자별(탈퇴) 사진 정보 삭제
	public void deletePhotoRecordsByUserNo(int userNo, String uploadDir) throws SQLException {
		String sql = "SELECT file_path FROM Photos WHERE user_no = ?";
		String deleteSql = "DELETE FROM Photos WHERE user_no = ?";
		try (Connection conn = DBUtil.getConnection()) {
			// 파일 경로를 가져오기 위한 쿼리 실행
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userNo);
				try (ResultSet rs = pstmt.executeQuery()) {
					while (rs.next()) {
						String filePath = rs.getString("file_path");
						File file = new File(uploadDir, new File(filePath).getName());
						if (file.exists()) {
							if (file.delete()) {
								System.out.println("파일 삭제 성공");
							} else {
								System.out.println("파일 삭제 실패");
							}
						} else {
							System.out.println("파일이 존재하지 않음");
						}
					}
				}
			}
			// 사진 정보를 삭제하기 위한 쿼리 실행
			try (PreparedStatement deletePstmt = conn.prepareStatement(deleteSql)) {
				deletePstmt.setInt(1, userNo);
				deletePstmt.executeUpdate();
			}
		} catch (SQLException e) {
			System.out.println("데이터베이스 작업 중 오류 발생: " + e.getMessage());
			throw e; // 예외를 다시 던져서 호출자에게 알림
		}
	}
}
