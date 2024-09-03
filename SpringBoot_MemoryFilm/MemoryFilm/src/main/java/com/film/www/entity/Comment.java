package com.film.www.entity;


import java.sql.Timestamp;

public class Comment {
    private int no;
    private int postNo;
    private int userNo;
    private String content;
    private Integer parentNo;  // NULL이 될 수 있음
    private Timestamp createdAt;

    // Getters and Setters
}
