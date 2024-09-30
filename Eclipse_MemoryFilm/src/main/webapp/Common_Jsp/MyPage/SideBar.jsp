
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    // 현재 페이지를 추적하는 매개변수
    String currentPage = request.getParameter("page");
    if (currentPage == null) {
        currentPage = "info"; // 기본값: 내 정보 수정 페이지
    }
    request.setAttribute("currentPage", currentPage);
%>
<div class="sidebar">
    <h3>
        <c:choose>
            <c:when test="${currentPage == 'info'}">내 정보 수정</c:when>
            <c:when test="${currentPage == 'likedPosts'}">좋아요<br> 게시글</c:when>
            <c:when test="${currentPage == 'myPosts'}">작성한<br> 게시글</c:when>
            <c:when test="${currentPage == 'myComments'}">작성한 댓글</c:when>
            <c:when test="${currentPage == 'deleteAccount'}">탈퇴하기</c:when>
            <c:otherwise>내 정보 관리</c:otherwise>
        </c:choose>
    </h3>
    <a href="<c:url value='/Html/MyPage.jsp' />?page=info">내 정보 수정</a>
    <a href="<c:url value='/LikedPosts.post' />?page=likedPosts">좋아요 게시글</a>
    <a href="<c:url value='/myPosts.post' />?page=myPosts">작성한 게시글</a>
    <a href="<c:url value='/myComments.comment' />?page=myComments">작성한 댓글</a>
    <a href="<c:url value='/Html/MyPageFile/DeleteAccount.jsp' />?page=deleteAccount">탈퇴하기</a>
</div>
