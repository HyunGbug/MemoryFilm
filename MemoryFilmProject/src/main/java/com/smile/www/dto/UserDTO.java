package com.smile.www.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;

public class UserDTO {
	private int no;
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String profileImageUrl;
	private String memo;
	private Timestamp createdAt;
	private String role; // 추가된 필드

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileUrl() {//디코딩해서 프로필 이미지 url 가져오기
		try {
			return URLDecoder.decode(profileImageUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("디코딩 실패");
			e.printStackTrace();
			return null;
		}
	}

	public void setProfileUrl(String profileUrl) { // 인코딩 해서 저장
		try {
			this.profileImageUrl = URLEncoder.encode(profileUrl, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			System.out.println("인코딩 실패");
			e.printStackTrace();
		}
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
