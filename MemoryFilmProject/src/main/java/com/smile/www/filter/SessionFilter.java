package com.smile.www.filter;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionFilter implements Filter {

	// 필터링을 거치지 않을 URL 패턴 정의
	private static final Pattern EXCLUDED_URLS = Pattern
			.compile (".*/Html/LoginForm_UI\\.jsp|" +
			        ".*/Html/Join_Membership\\.jsp|" +
			        ".*/Html/About\\.jsp|" +
			        ".*/Html/FindUserAccount/.*\\.jsp|" + // FindUserAccount 폴더 내 모든 JSP 파일 제외
			        ".*/login\\.user|" +
			        ".*/signup\\.user|" +
			        ".*/logout\\.user|" +
			        ".*/check\\-id\\.user|" +
			        ".*/check\\-username\\.user" +
			        ".*/check\\-email\\.user" + 
			        ".*/resetPassword\\.user|" + // resetPassword.user 제외
		            ".*/EmailUtil.*" // EmailUtil 페이지 제외
			    );

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String requestURI = req.getRequestURI();
		HttpSession session = req.getSession(false);

		// 사용자가 로그인된 상태인지 확인
		boolean loggedIn = (session != null && session.getAttribute("user") != null);
		// 정적 리소스 및 특정 페이지 요청인지 확인
		boolean excludedRequest = EXCLUDED_URLS.matcher(requestURI).matches();

		// 캐시 비활성화 헤더 설정
		res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		res.setHeader("Pragma", "no-cache");
		res.setDateHeader("Expires", 0);

		// 로그인된 상태이거나 정적 리소스 요청인 경우 필터링을 거치지 않음
		if (loggedIn || excludedRequest) {
			chain.doFilter(request, response);
		} else {
			// 그렇지 않으면 로그인 페이지로 리다이렉트
			res.sendRedirect(req.getContextPath() + "/Html/LoginForm_UI.jsp");
			System.out.println("세션 존재 x - 로그인 필요");
		}
	}
}
