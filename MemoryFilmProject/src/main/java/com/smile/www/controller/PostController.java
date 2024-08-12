package com.smile.www.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smile.www.dto.PostDTO;
import com.smile.www.dto.UserDTO;
import com.smile.www.service.PostService;

public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PostService postService;

	public PostController() {
		super();
		postService = new PostService();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		String viewPage = null; // 초기화

		try {
			boolean success;
			System.out.println(action);
			switch (action) {
			case "/write.post":
				success = postService.addPost(request, response);
				if (success) {
					response.sendRedirect("list.post");
				} else {
					viewPage = "ErrorFile/error.jsp";
				}
				break;

			case "/updateForm.post":
				viewPage = showUpdateForm(request, response);
				break;
			case "/update.post":
				int postNo = postService.updatePost(request, response);
				response.sendRedirect("view.post?postNo=" + postNo);
				return;
			case "/delete.post":
				postService.deletePost(request, response);
				response.sendRedirect("list.post");
				return;
			case "/list.post":
				postService.listPosts(request, response);
				return;
			case "/view.post":
				viewPage = handleViewPost(request, response);
				break;
			// 내가 작성한 게시글 보기
			case "/myPosts.post":
				viewPage = handleMyPosts(request, response);
				break;
			//마이 페이지에서 게시글 삭제
			case "/myPostsDelete.post":
				postService.deletePost(request, response);
				response.sendRedirect("myPosts.post");
				return;
			//마이 페이지에서 게시글 수정
			case "/myPostsUpdateForm.post":
				viewPage = PostShowUpdateForm(request, response);
				 break;
			case "/myPostsUpdate.post":
				postService.updatePost(request, response);
				response.sendRedirect("myPosts.post");
				return;
			// 좋아요한 게시글 보기 (마이페이지 용)
			case "/LikedPosts.post":
				viewPage = handleLikedPosts(request, response);
				break;
			// 커뮤니티 페이지에서 좋아요 상태 가져오기
			case "/getLiked.post":
				postService.getLikedPostStatus(request, response);
				break;
			// 좋아요 수정
			case "/toggleLike.post": // 좋아요
				postService.toggleLike(request, response);
				return; // JSON 응답을 직접 반환하므로 여기서 종료

			default:
				viewPage = "ErrorFile/Error.jsp";
				request.setAttribute("error", "Invalid action");
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("error", "데이터베이스 연결 에러");
			RequestDispatcher dispatcher = request.getRequestDispatcher("ErrorFile/Error.jsp");
			dispatcher.forward(request, response);
		}
		if (viewPage != null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}

	// view페이지
	private String handleViewPost(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		PostDTO post = postService.viewPost(request);
		request.setAttribute("post", post);

		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");
		boolean isAuthor = currentUser != null && post.getUsername().equals(currentUser.getUserName());
		request.setAttribute("isAuthor", isAuthor);

		return "/Html/CommunityFile/Cview.jsp";

	}

	// 게시물 수정 폼 데이터 저장
	private String showUpdateForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		PostDTO post = postService.getPostById(postNo);
		request.setAttribute("post", post);

		return "/Html/CommunityFile/Cupdate.jsp";
	}
	
	//마이 페이지에서 수정 폼 데이터 저장
	private String PostShowUpdateForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		PostDTO post = postService.getPostById(postNo);
		request.setAttribute("post", post);
		
		return "/Html/MyPageFile/Pupdate.jsp";
	}
	

	// 본인이 작성한 게시글 보기
	private String handleMyPosts(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		HttpSession session = request.getSession();
		UserDTO currentUser = (UserDTO) session.getAttribute("user");
		if (currentUser == null) {
			return "Html/LoginForm_UI.jsp";
		}
		request.setAttribute("posts", postService.getPostsByUser(currentUser.getNo()));
		return "/Html/MyPageFile/MyPosts.jsp";
	}

	// 좋아요 했던 게시글 보기
	  private String handleLikedPosts(HttpServletRequest request, HttpServletResponse response)
	            throws SQLException, ServletException, IOException {
	        HttpSession session = request.getSession();
	        UserDTO currentUser = (UserDTO) session.getAttribute("user");
	        if (currentUser == null) {
	            response.sendRedirect("Html/LoginForm_UI.jsp");
	            return null;
	        }
	        request.setAttribute("posts", postService.getLikedPostsByUser(currentUser.getNo()));
	        System.out.println("postService로 본인이 좋아요 한 게시글 보는 메서드 실행 ");
	        return "/Html/MyPageFile/LikedPosts.jsp";
	    }
}
