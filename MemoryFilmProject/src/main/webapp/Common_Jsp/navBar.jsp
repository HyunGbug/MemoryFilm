<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav>
    <a href="<c:url value='/Html/HomePage(Login)_UI.jsp' />" class="nav-link">Home</a>
    <a href="<c:url value='/list.post' />" class="nav-link">Community</a>
    <a href="<c:url value='/Html/MyPage.jsp' />" class="nav-link">My Page</a>
    <a href="<c:url value='/Html/About.jsp' />" class="nav-link">About</a>
    <c:choose>
        <c:when test="${not empty sessionScope.user}">
            <a href="#" onclick="confirmLogout()" class="nav-link">LogOut</a>
        </c:when>
        <c:otherwise>
            <a href="<c:url value='/loginPage' />" class="nav-link">LogIn</a>
        </c:otherwise>
    </c:choose>
</nav>

<script>
    function confirmLogout() {
        if (confirm('로그아웃 하시겠습니까?')) { //alert와 비슷한 내장 함수로, 사용자의 응답에 따라 boolean으로 반환됌
            window.location.href = '<c:url value="/logout.user"/>';
        }
    }
</script>
