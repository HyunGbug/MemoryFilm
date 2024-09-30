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
			onclick="window.location.href='${pageContext.request.contextPath}/HomePage(LoginX)_UI.jsp'">Home</button>
		<h2>Finding The Password</h2>
		<h3>회원가입한 아이디와 이메일을 입력하세요.</h3>
		<form id="resetPasswordForm" class="form">
			<input type="text" id="id" name="id" class="theme" placeholder="User ID" autocomplete="username"
				required> <input type="email" id="email" name="email" class="theme" placeholder="Email"
				autocomplete="email" required>
			<button type="submit" class="theme next">Next</button>
			<a href="${pageContext.request.contextPath}/Html/FindUserAccount/Find_Id.jsp" class="forgot">Forgot
				your Id?</a>
		</form>
		<button class="theme LoginForm" type="button"
			onclick="window.location.href = '${pageContext.request.contextPath}/Html/LoginForm.jsp'">
			Login</button>
	</div>
	<script type="module">
    import GradientBG from '<c:url value="/JS/gradient-bg.js" />';
    const gradient = new GradientBG();
    gradient.generate();
</script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		var contextPath = "${pageContext.request.contextPath}";
		var resetPasswordScriptUrl = "<c:url value='/JS/resetPassword.js' />";

		$.getScript(resetPasswordScriptUrl).done(function(script, textStatus) {
			console.log("resetPassword.js loaded successfully.");
		}).fail(function(jqxhr, settings, exception) {
			console.log("Failed to load resetPassword.js.");
		});
	</script>
</body>
</html>