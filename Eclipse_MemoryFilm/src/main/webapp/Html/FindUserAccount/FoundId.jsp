<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Login</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Login/FindAccountCss.css' />">
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
</head>
<body>
	<div class="card">
	<button class="theme home" type="button"
			onclick="window.location.href='${pageContext.request.contextPath}/Html/FindUserAccount/Find_Password.jsp'">Previous </button>
		<c:if test="${not empty userId}">
			<h2>Your User ID : ${userId}</h2>
		</c:if>
		<c:if test="${empty userId}">
			<h3>해당 계정은 존재하지 않습니다. 이메일을 다시 확인해주세요.</h3>
		</c:if>
		<h3>User ID Found</h3>
		<button class="theme LoginForm" type="button"
			onclick="window.location.href='${pageContext.request.contextPath}/Html/LoginForm.jsp'">Login</button>
	</div>
	<script type="module">
    import GradientBG from '${pageContext.request.contextPath}/JS/gradient-bg.js';
    const gradient = new GradientBG();
    gradient.generate();
</script>
</body>
</html>
