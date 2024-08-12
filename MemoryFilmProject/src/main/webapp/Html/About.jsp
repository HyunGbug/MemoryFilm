<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: About</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Home/AboutStyle.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/Css/Home/HomeStyle.css">
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
		<h1 style="margin-bottom: 8px;">Memory Film 소개</h1>
		<strong>
			<p>Memory Film은 필름 카메라로 찍은 사진을 현상 후 보관해 관리하고, 사람들과 찍은 사진을 공유하는 공간입니다.</p>
		</strong>
		<div class="content-wrapper">
			<p>
				안녕하세요.<br> Memory Film 웹 사이트를 개발한 정현지입니다.<br> 제가 이 사이트를 만든 이유를 설명드리겠습니다.
			</p>
			<img src="${pageContext.request.contextPath}/Imgs/About/IntroduceImg.jpg" alt="Developer Photo">
		</div>
		<p class="content">필름 카메라에 관심이 생겨, 카메라를 구매 후 추억을 남기며 사진을 찍으러 다녔습니다.<br>가족, 친구 또는 풍경 등을 찍으며 제 추억을
			기록했습니다. 보통 필름 현상 후 필름은 앨범에 관리하고 현상한 이미지 파일은 갤러리에 저장해 관리했으나 일반 카메라와 필름 카메라로 찍은 사진들의<br>구분이 어려워 구분할
			공간이 있으면 좋겠다 라는 생각이 들었고, 필름 카메라에 관심을 가진 <br>사람들끼리의 정보 공유하는 공간도 있으면 하는 바램이 생겨 제작하게 되었습니다.</p>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="${pageContext.request.contextPath}/JS/NavBar_Color.js"></script>
</body>
</html>