package com.smile.www.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smile.www.service.UserService;

@MultipartConfig
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserService userService;

	public UserController() {
		super();
		userService = new UserService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doAction(request, response);
	}

	protected void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String command = request.getServletPath();
		String viewPage = null;

		try {
			switch (command) {
			// 회원가입 시 검증
			case "/check-id.user":
				userService.checkId(request, response);
				return;

			case "/check-username.user":
				userService.checkUsername(request, response);
				return;

			case "/check-email.user":
				userService.checkEmail(request, response);
				return;

			// 본인 계정 수정 시 검증
			case "/check-update-id.user":
				userService.checkUpdateId(request, response);
				return;

			case "/check-update-username.user":
				userService.checkUpdateUsername(request, response);
				return;

			case "/check-update-email.user":
				userService.checkUpdateEmail(request, response);
				return;
				
			//본인이 좋아요 한 게시글 보기
			 case "/userLikedPosts.user":
                  break;	

			// 회원가입, 로그인, 로그아웃 등
			case "/signup.user":
				userService.signup(request, response);
				return;

			case "/login.user":
				userService.login(request, response);
				return;

			case "/logout.user":
				userService.logout(request, response);
				return;

			case "/update.user":
				userService.update(request, response);
				return;

			case "/delete.user":
				userService.delete(request, response);
				return;

			case "/saveNote.user":
				userService.updateMemo(request, response);
				return;

			// 계정 아이디, 비번 까먹었을 때
			case "/resetPassword.user":
				userService.resetPassword(request, response);
				return;

			case "/findUserId.user":
				userService.findUserId(request, response);
				return;

			default:
				viewPage = "/ErrorFile/404.jsp";
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ServletException(e);
		} catch (IOException | ServletException e) {
			if (e.getCause() instanceof javax.servlet.ServletException) {
				request.setAttribute("errorMessage", "파일 크기가 제한을 초과했습니다. 다시 시도해주세요.");
				request.setAttribute("id", request.getParameter("id"));
				request.setAttribute("username", request.getParameter("username"));
				request.setAttribute("email", request.getParameter("email"));
				request.setAttribute("password", request.getParameter("password"));
				viewPage = "/Html/Join_Membership.jsp";
			} else {
				throw e;
			}
		}
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}
}
