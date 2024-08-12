<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Login</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Login/FindAccountCss.css' />">
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
<style>
.theme.LoginForm {
	cursor: pointer;
	position: absolute;
	top: 10px;
	right: 17px;
	width: auto;
	background: #0420f045;
	color: #fff;
	padding: 5px 16px;
	font-size: 14px;
	border-radius: 7px;
	box-sizing: border-box;
	height: 40px;
}
</style>

</head>
<body>
<div class="card">
		<button class="theme home" type="button"
			onclick="window.location.href='${pageContext.request.contextPath}/Html/FindUserAccount/Find_Password.jsp'">Previous </button>
		<h2>Finding ID</h2>
		<h3>회원가입한 이메일을 입력하세요.</h3>
		<form action="${pageContext.request.contextPath}/findUserId.user" method="post" class="form">
				<input type="email" id="email" name="email" class="theme" placeholder="Email"
					autocomplete="email" required>
			<button type="submit" class="theme next">Next</button>
		</form>
		<button class="theme LoginForm" type="button"
			onclick="window.location.href = '${pageContext.request.contextPath}/Html/LoginForm.jsp'">
			Login</button>
	</div>
	<c:if test="${not empty error}">
		<script>
			alert('${error}');
		</script>
	</c:if>
	
	<script type="module">
    import GradientBG from '${pageContext.request.contextPath}/JS/gradient-bg.js';
    const gradient = new GradientBG();
    gradient.generate();
</script>
</body>
</html>