<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Join</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Login/LoginCSS.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Login/MembershipCSS.css' />">
<script src="https://kit.fontawesome.com/3b644b33f6.js"></script>
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
<style>
.password-container .toggle-password {
	right: 18px;
}
</style>
</head>
<body>
	<div class="card">
		<button class="theme home" type="button"
			onclick="window.location.href='<c:url value='/HomePage(LoginX)_UI.jsp' />'">Home</button>
		<h2>Sign Up</h2>
		<h3>Create your account</h3>
		<form id="form" action="<c:url value='/signup.user' />" method="POST" class="MemberForm"
			enctype="multipart/form-data">
			<div class="profile-picture-wrapper">
				<div class="profile-picture" id="profilePicturePreview">
					<c:choose>
						<c:when test="${not empty sessionScope.user.profileUrl}">
							<img src="<c:out value='${sessionScope.user.profileUrl}' />" alt="Profile">
						</c:when>
						<c:otherwise>
							<img src="<c:url value='/DefaultProfile/defaultProfile.png' />" alt="Profile">
						</c:otherwise>
					</c:choose>
				</div>
				<label for="profilePicture" class="file-label">Choose Your Profile</label> <input type="file"
					id="profilePicture" name="profilePicture" accept="image/*">
			</div>
			<div class="input-group">
				<label class="required-field" for="id"></label> <input type="text" id="id" class="theme"
					name="id" placeholder="ID : 15자리 이하" value="${id}" autocomplete="name" required>
				<button type="button" class="valBtn" id="checkIdBtn">Check</button>
			</div>
			<div class="input-group">
				<label class="required-field" for="username"></label> <input type="text" id="username"
					class="theme" name="username" placeholder="User name : 8자 이하, 한글 가능" value="${username}"
					autocomplete="username" required>
				<button type="button" class="valBtn" id="checkUsernameBtn">Check</button>
			</div>
			<div class="input-group">
				<label class="required-field" for="email"></label> <input type="email" id="email" class="theme"
					name="email" placeholder="Email" value="${email}" autocomplete="email" required>
				<button type="button" class="valBtn" id="checkEmailBtn">Check</button>
			</div>
			<div class="input-group">
				<label class="required-field" for="password"></label>
				<div class="password-container">
					<input type="password" id="password" class="theme" name="password"
						placeholder="Password : 8~20자리 이하, 문자, 숫자, 특수 기호 포함" autocomplete="off" required> <span
						class="toggle-password" onclick="togglePasswordVisibility('password', 'passwordIcon')">
						<i class="fa-sharp fa-regular fa-eye-slash" id="passwordIcon"></i>
					</span>
				</div>
			</div>
			<div class="input-group">
				<label class="required-field" for="passwordChk"></label>
				<div class="password-container">
					<input type="password" id="passwordChk" class="theme" name="passwordChk"
						placeholder="Password Check" autocomplete="off" required> <span
						class="toggle-password" onclick="togglePasswordVisibility('passwordChk', 'passwordChkIcon')">
						<i class="fa-sharp fa-regular fa-eye-slash" id="passwordChkIcon"></i>
					</span>
				</div>
			</div>
			<button type="submit" class="theme login">Sign Up</button>
		</form>
	</div>
	<!-- 모듈 스크립트 -->
	<script type="module">
        import GradientBG from '<c:url value="/JS/gradient-bg.js" />';
        const gradient = new GradientBG();
        gradient.generate();
    </script>
	<script>
        var contextPath = '<%= request.getContextPath() %>';
    </script>
	<script src="<c:url value='/JS/Membership_Validation.js' />"></script>
</body>
</html>
