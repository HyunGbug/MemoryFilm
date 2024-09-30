package com.smile.www.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smile.www.service.UserService;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    public LoginServlet() {
        this.userService = new UserService();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("로그인 서블릿 실행");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberUserId".equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                    request.setAttribute("rememberUserId", cookie.getValue());
                    request.setAttribute("rememberMeChecked", "checked");
                }
            }
        }
        // 캐시 무효화 헤더 설정
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        request.getRequestDispatcher("/Html/LoginForm_UI.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	System.out.println("로그인 서블릿 포스트 메서드 실행");
            userService.login(request, response);
        } catch (Exception e) {
            throw new ServletException("로그인 처리 중 오류 발생", e);
        }
    }
}