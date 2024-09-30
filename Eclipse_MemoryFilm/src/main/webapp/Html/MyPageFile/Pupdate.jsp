<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: My Page</title>
<!-- 부트스트랩 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Community/CupdateStyle.css' />">
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
			<form action="${pageContext.request.contextPath}/myPostsUpdate.post" method="post">
				<input type="hidden" name="postNo" value="${post.no}">
				<div class="post">
					<div class="post-header">
						<img src="${post.profileUrl}" alt="${post.username}" class="profile-img"> <span
							class="username">${post.username}</span> <span class="hit-count">조회수: ${post.hit}</span>
					</div>
					<div class="post-content">
						<div class="post-dates mt-3">
							<span class="post-date">작성일: ${post.createdAt}</span>
							<c:if test="${not empty post.updatedAt}">
								<span class="post-date">수정일: ${post.updatedAt}</span>
							</c:if>
						</div>
						<div class="mb-2">
							<label for="title" class="form-label">제목</label> <input type="text" name="title" id="title"
								value="${post.title}" class="form-control">
						</div>
						<hr>
						<div class="mb-2">
							<label for="content" class="form-label">내용</label>
							<textarea name="content" id="content" rows="5" class="form-control">${post.content}</textarea>
						</div>
						<c:if test="${not empty post.photoUrls}">
							<c:choose>
								<c:when test="${fn:length(post.photoUrls) == 1}">
									<div>
										<img class="d-block w-100" src="${post.photoUrls[0]}" alt="Post Photo">
										<div class="form-check mt-2">
											<input class="form-check-input" type="checkbox" name="deletePhotos" id="deletePhoto0"
												value="${post.photoUrls[0]}"> <label class="form-check-label" for="deletePhoto0">삭제</label>
										</div>
									</div>
								</c:when>
								<c:otherwise>
									<div id="carouselExampleIndicators${post.no}" class="carousel slide"
										data-bs-interval="false">
										<div class="carousel-inner">
											<c:forEach var="photoUrl" items="${post.photoUrls}" varStatus="status">
												<div class="carousel-item ${status.first ? 'active' : ''}">
													<img class="d-block w-100" src="${photoUrl}" alt="Post Photo">
													<div class="form-check mt-2">
														<input class="form-check-input" type="checkbox" name="deletePhotos"
															id="deletePhoto${status.index}" value="${photoUrl}"> <label
															class="form-check-label" for="deletePhoto${status.index}">삭제</label>
													</div>
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
					<div class="text-muted">사진 삭제를 원하시면 각 사진 아래의 삭제 버튼을 클릭하고 저장을 누르세요.</div>
					<div class="post-footer d-flex justify-content-between align-items-center">
						<button type="submit" class="btn btn-primary">저장</button>
						<button type="button" class="btn btn-secondary"
								onclick="location.href='${pageContext.request.contextPath}/myPosts.post'">취소</button>
					</div>
				</div>
			</form>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- jQuery 먼저 로드 -->
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"></script>
	<!-- Bootstrap 다음에 로드 -->
	<script>
		$(document).ready(function() {
			$('[id^="carouselExampleIndicators"]').carousel({
				interval : false
			});
		});
	</script>
</body>
</html>
