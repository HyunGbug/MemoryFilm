<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Login</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Login/LoginCSS.css' />">
<script src="https://kit.fontawesome.com/3b644b33f6.js"></script>
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
<style>
.theme.signup {
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

.rememberId, rememberMe {
	color: #ffffff;
	cursor: pointer;
}

.password-container .toggle-password {
	right: 30px;
}
</style>
</head>
<body>
	<div class="card">
		<button class="theme home" type="button"
			onclick="window.location.href='${pageContext.request.contextPath}/HomePage(LoginX)_UI.jsp'">Home</button>
		<h2>Log In</h2>
		<h3>Access your account</h3>
		<form action="${pageContext.request.contextPath}/login.user" method="post" class="form">
			<input type="text" id="id" name="id" class="theme" placeholder="User ID" autocomplete="username"
				required>
			<div class="password-container">
				<input type="password" id="password" name="password" class="theme" placeholder="Password"
					autocomplete="current-password" required> <span class="toggle-password"
					onclick="togglePasswordVisibility()"> <i class="fa-sharp fa-regular fa-eye-slash"
					id="passwordIcon"></i>
				</span>
			</div>
			<div class="strength" id="strength"></div>
			<button type="submit" class="theme login">Log In</button>
			<div>
				<input type="checkbox" id="rememberMe" name="rememberMe"> <label for="rememberMe"
					class="rememberId">아이디 기억하기</label>
			</div>
			<a href="${pageContext.request.contextPath}/Html/FindUserAccount/Find_Password.jsp"
				class="forgot-password">Forgot your Id/Password?</a>
		</form>
		<button class="theme signup" type="button"
			onclick="window.location.href = '${pageContext.request.contextPath}/Html/Join_Membership.jsp'">
			create Account</button>
	</div>
	<script type="module">
    import GradientBG from '${pageContext.request.contextPath}/JS/gradient-bg.js';
    const gradient = new GradientBG();
    gradient.generate();
</script>
	<script src="<c:url value='/JS/Login_Validation.js' />"></script>
	<script src="<c:url value='/JS/Login_RememberId.js' />"></script>
	<c:if test="${param.error eq 'true'}">
		<script>
			 showError('아이디와 비밀번호를 다시 확인해주세요.'); 
			
		</script>
	</c:if>
</body>
</html>