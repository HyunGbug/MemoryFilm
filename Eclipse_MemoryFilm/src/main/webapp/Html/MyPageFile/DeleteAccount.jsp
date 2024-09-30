<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: MyPage</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/MyPage/DeleteAccountStyle.css' />">
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
			<h2>탈퇴하기</h2>
			<div>
				<form id="deleteAccountForm" action="<c:url value='/delete.user' />" method="post">
					<div class="input-group">
						<label for="password">비밀번호</label> <input type="password" id="password" name="password"
							required>
					</div>
					<button type="button" class="submitBtn" onclick="confirmDeletion()">탈퇴하기</button>
				</form>
			</div>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script>
	function confirmDeletion() {
	    if (confirm('탈퇴하시면 모든 정보가 삭제됩니다. \n정말 탈퇴하시겠습니까?')) {
	        var form = document.getElementById('deleteAccountForm');
	        var formData = new FormData(form);

	        fetch(form.action, {
	            method: 'POST',
	            body: formData
	        })
	        .then(response => response.json())
	        .then(data => {
	            if (data.success) {
	                alert(data.message);
	                window.location.href = '<c:url value="/HomePage(LoginX)_UI.jsp" />';
	            } else {
	                alert(data.message);
	            }
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert('계정 삭제 중 오류가 발생했습니다. 다시 시도해주세요.');
	        });
	    }
	}
	</script>
</body>
</html>