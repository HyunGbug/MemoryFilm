<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film ::My Page</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/MyPage/ModifyInfo.css' />">
<script src="https://kit.fontawesome.com/3b644b33f6.js"></script>
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
</head>
<body>
	<header>
		<div class="logo">
			Memory <span>Film</span>
		</div>
		<jsp:include page="/Common_Jsp/navBar.jsp" />
	</header>
	<main>
		<jsp:include page="/Common_Jsp/MyPage/SideBar.jsp" />
		<div class="edit-card">
			<h2>내 정보 수정</h2>
			<form id="updateForm" action="<c:url value='/update.user' />" method="post"
				enctype="multipart/form-data">
				<div class="profile-picture-wrapper">
					<div class="profile-picture" id="profilePicturePreview">
						<img src="<c:out value='${sessionScope.user.profileUrl}'/>" alt="Profile">
					</div>
					<label for="profilePicture" class="file-label">프로필 사진 수정</label> <input type="file"
						id="profilePicture" name="profilePicture" accept="image/*" style="display: none;">
				</div>
				<div class="input-group">
					<div class="input-label">
						<label for="id">아이디</label>
						<button type="button" class="valBtn" id="checkIdBtn">Check</button>
					</div>
					<input type="text" id="id" name="id" value="<c:out value='${sessionScope.user.userId}'/>"
						autocomplete="name" required>
				</div>
				<div class="input-group">
					<div class="input-label">
						<label for="username">닉네임</label>
						<button type="button" class="valBtn" id="checkUsernameBtn">Check</button>
					</div>
					<input type="text" id="username" name="username"
						value="<c:out value='${sessionScope.user.userName}'/>" autocomplete="username" required>
				</div>
				<div class="input-group">
					<div class="input-label">
						<label for="email">이메일</label>
						<button type="button" class="valBtn" id="checkEmailBtn">Check</button>
					</div>
					<input type="email" id="email" name="email" value="<c:out value='${sessionScope.user.email}'/>"
						autocomplete="email" required>
				</div>
				<div class="input-group">
					<div class="input-label">
						<label for="password">비밀번호</label>
					</div>
					<div class="password-container">
						<input type="password" id="password" name="password" autocomplete="off" required> <span
							class="toggle-password" onclick="togglePasswordVisibility('password', 'passwordIcon')">
							<i class="fa-sharp fa-regular fa-eye-slash" id="passwordIcon"></i>
						</span>
					</div>
				</div>
				<div class="input-group">
					<div class="input-label">
						<label for="passwordChk">비밀번호 확인</label>
					</div>
					<div class="password-container">
						<input type="password" id="passwordChk" name="passwordChk" autocomplete="off" required>
						<span class="toggle-password"
							onclick="togglePasswordVisibility('passwordChk', 'passwordChkIcon')"> <i
							class="fa-sharp fa-regular fa-eye-slash" id="passwordChkIcon"></i>
						</span>
					</div>
				</div>
				<button type="submit" class="submitBtn">변경 사항 저장</button>
			</form>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="<c:url value='/JS/NavBar_Color.js' />"></script>
	<script>
        var contextPath = '<%= request.getContextPath()%>';
	</script>
	<script src="<c:url value='/JS/MyPage_Modify_Validation.js' />"></script>
</body>
</html>