<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Community</title>
<!-- 부트스트랩 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Community/CommunityStyle.css' />">
<script src="https://kit.fontawesome.com/3b644b33f6.js"></script>
<!-- FontAwesome 로드 -->
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
		<div class="post-container">
			<div class="create-post">
				<button class="createBtn"
					onclick="location.href='${pageContext.request.contextPath}/Html/CommunityFile/Cwrite.jsp'">
					<i class="fa-solid fa-pen-to-square"></i> 글쓰기
				</button>
			</div>
			<c:choose>
				<c:when test="${empty posts}">
					<h2>게시물이 없습니다. 첫 번째 게시물을 작성해보세요!</h2>
				</c:when>
				<c:otherwise>
					<c:forEach var="post" items="${posts}">
						<div class="post-card" data-post-no="${post.no}">
							<div class="post">
								<div class="post-header">
									<img src="${post.profileUrl}" alt="${post.username}" class="profile-img"> <span
										class="username">${post.username}</span> <span class="hit-count">조회수 : ${post.hit}</span>
								</div>
								<div class="post-content" onclick="viewPost(${post.no})">
									<h2>${post.title}</h2>
									<p class="description">글의 내용을 보고싶다면 게시글을 클릭해주세요!</p>
									<c:if test="${not empty post.photoUrls}">
										<c:choose>
											<c:when test="${fn:length(post.photoUrls) == 1}">
												<img class="d-block w-100" src="${post.photoUrls[0]}" alt="Post Photo">
											</c:when>
											<c:otherwise>
												<div id="carouselExampleIndicators${post.no}" class="carousel slide"
													data-bs-interval="false">
													<div class="carousel-inner">
														<c:forEach var="photoUrl" items="${post.photoUrls}" varStatus="status">
															<div class="carousel-item ${status.index == 0 ? 'active' : ''}">
																<img class="d-block w-100" src="${photoUrl}" alt="Post Photo">
															</div>
														</c:forEach>
													</div>
													<button class="carousel-control-prev" type="button"
														data-bs-target="#carouselExampleIndicators${post.no}" data-bs-slide="prev"
														onclick="event.stopPropagation()">
														<span class="carousel-control-prev-icon" aria-hidden="true"></span> <span
															class="visually-hidden">Previous</span>
													</button>
													<button class="carousel-control-next" type="button"
														data-bs-target="#carouselExampleIndicators${post.no}" data-bs-slide="next"
														onclick="event.stopPropagation()">
														<span class="carousel-control-next-icon" aria-hidden="true"></span> <span
															class="visually-hidden">Next</span>
													</button>
												</div>
											</c:otherwise>
										</c:choose>
									</c:if>
								</div>
								<span class="post-date write-date">작성일 : ${post.createdAt}</span>
								<c:if test="${not empty post.updatedAt}">
									<span class="post-date">수정일 : ${post.updatedAt}</span>
								</c:if>
							</div>
							<div class="icon">
								<i class="fa-regular fa-heart like-btn" data-post-no="${post.no}"></i> <i
									class="fa-regular fa-message comment-btn"  onclick="saveScrollPositionAndViewPost(${post.no})" data-post-no="${post.no}"></i>
							</div>
						</div>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="scroll-to-top" onclick="scrollToTop()">
			<i class="fa-solid fa-arrow-up"></i>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="<c:url value='/JS/NavBar_Color.js' />"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- jQuery 먼저 로드 -->
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"></script>
	<!-- Bootstrap 다음에 로드 -->
	<script>
        var contextPath = '<%=request.getContextPath()%>';
    </script>
	<script src="<c:url value='/JS/CommunityJs/CommunityFunction.js' />"></script>
</body>
</html>
