package com.smile.www.dto;

import java.io.Serializable;

public class PhotoDTO  implements Serializable {
	 private static final long serialVersionUID = 1L;
	
    private int no;
    private int userNo;
    private String filePath;
    private String uploadDate;
    private String cameraInfo;
    private String lensInfo;
    private String locationInfo;
    private String memo;

    // Getters and Setters
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(String cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    public String getLensInfo() {
        return lensInfo;
    }

    public void setLensInfo(String lensInfo) {
        this.lensInfo = lensInfo;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
