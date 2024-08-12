package com.smile.www.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smile.www.service.UserService;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserService userService;

    public LogoutServlet() {
        this.userService = new UserService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	System.out.println("로그아웃 서블릿 실행");
            userService.logout(request, response);
        } catch (Exception e) {
            throw new ServletException("로그아웃 중 오류 발생", e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}