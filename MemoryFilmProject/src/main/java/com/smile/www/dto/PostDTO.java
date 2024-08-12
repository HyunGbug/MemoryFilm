package com.smile.www.dto;

import java.util.List;

public class PostDTO {

	private int no;
	private int userNo;
	private int hit; // 조회수
	private String title;
	private String content;
	private String createdAt;//생성 시간
	private String updatedAt;//수정 시간
	 private List<Integer> photoNos; // 게시물에 포함된 사진 번호 목록
	private List<String> photoUrls; // 게시물에 포함된 사진 URL 목록
	private String profileUrl; // 프로필 URL
	private String username; // 사용자 닉네임

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}


	public List<Integer> getPhotoNos() {
		return photoNos;
	}
	
	public void setPhotoNos(List<Integer> photoNos) {
		this.photoNos = photoNos;
	}
	public List<String> getPhotoUrls() {
		return photoUrls;
	}

	public void setPhotoUrls(List<String> photoUrls) {
		this.photoUrls = photoUrls;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}
}
