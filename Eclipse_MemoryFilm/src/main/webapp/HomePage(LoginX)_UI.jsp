<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Home</title>
<link rel="stylesheet" href="Css/Home/HomeStyle.css">
<link rel="stylesheet" href="Css/Home/LoginX_HomeStyle.css">
 <link rel="icon" type="image/png" href="<c:url value='/Imgs/favicon/favicon.png' />">
</head>
<body>
	<header>
		<div class="logo">
			Memory <span>Film</span>
		</div>
		<jsp:include page="Common_Jsp/Ex_navBar.jsp" />
	</header>
	<main>
		<div class="bannerName">Record your memories here</div>
		<section class="first">
			<div class="outer">
				<div class="inner">
					<div class="bg one">
						<h2 class="section-heading">
							환영합니다.<br> 이곳은 당신의 추억을 기록하고 공유하는 <br> Memory Film 입니다.
						</h2>
					</div>
				</div>
			</div>
		</section>
		<section class="second">
			<div class="outer">
				<div class="inner">
					<div class="bg">
						<h2 class="section-heading">
							필름 카메라와의 추억을 <br> 사람들과 함께 나눠보세요!
						</h2>
					</div>
				</div>
			</div>
		</section>
		<section class="third">
			<div class="outer">
				<div class="inner">
					<div class="bg">
						<h2 class="section-heading">
							본인만의 프로필을 꾸미며 <br> 어떤 카메라와 함께 했는지 기록하세요!
						</h2>
					</div>
				</div>
			</div>
		</section>
		<section class="fourth">
			<div class="outer">
				<div class="inner">
					<div class="bg">
						<h2 class="section-heading">
							장소 / 카메라 / 필름 <br> 무엇과 함께 했는지 기록하세요!
						</h2>
					</div>
				</div>
			</div>
		</section>
		<section class="fifth">
			<div class="outer">
				<div class="inner">
					<div class="bg">
						<h2 class="section-heading">
							모든 준비는 끝났습니다. <br> 당신의 걸음을 기록해보세요!
						</h2>
						<svg id="startButton" viewBox="45 60 400 320" xmlns="http://www.w3.org/2000/svg">
                            <path fill="#C3A6A0"
								d="M 90 210 C 90 180 90 150 90 150 C 180 150 300 150 300 150 C 180 150 300 150 300 150 C 300 150 330 150 390 150 C 390 150 390 180 390 210 C 390 240 390 270 390 270 C 330 270 300 270 300 270 C 300 270 180 270 180 270 C 180 270 150 270 90 270 C 90 270 90 240 90 210"
								mask="url(#knockout-text)">
                            </path>
                            <mask id="knockout-text">
                                <rect width="100%" height="100%" fill="#fff" x="0" y="0" />
                                <text x="147" y="227" fill="#000">Start Now</text>
                            </mask>
                        </svg>
					</div>
				</div>
			</div>
		</section>
		<div class="scroll-down">
			<span>Scroll Down</span> <span class="arrows">🔻</span>
		</div>
	</main>
</body>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/Observer.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.5/ScrollTrigger.min.js"></script>
<script src="JS/LoginX_Page_img.js"></script>
<script src="JS/NavBar_Color.js"></script>
</html>