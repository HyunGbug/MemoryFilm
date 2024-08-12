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
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Community/CviewStyle.css' />">
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
			<div class="post">
				<button id="communityButton" class="previewBtn" onclick="goBackToCommunity()">
					<i class="fa-solid fa-list-ul"></i> 목록
				</button>
				<div class="post-header">
					<img src="${post.profileUrl}" alt="${post.username}" class="profile-img"> <span
						class="username">${post.username}</span> <span class="hit-count">조회수: ${post.hit}</span>
				</div>
				<div class="post-content">
					<h2>${post.title}</h2>
					<hr>
					<p>${post.content}</p>
					<c:if test="${not empty post.photoUrls}">
						<c:choose>
							<c:when test="${fn:length(post.photoUrls) == 1}">
								<div>
									<img class="d-block w-100" src="${post.photoUrls[0]}" alt="Post Photo">
								</div>
							</c:when>
							<c:otherwise>
								<div id="carouselExampleIndicators${post.no}" class="carousel slide"
									data-bs-interval="false">
									<div class="carousel-inner">
										<c:forEach var="photoUrl" items="${post.photoUrls}" varStatus="status">
											<div class="carousel-item ${status.first ? 'active' : ''}">
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
				<div class="post-date">
					<span class="post-date">작성일: ${post.createdAt}</span><br>
					<c:if test="${not empty post.updatedAt}">
						<span class="post-date">수정일: ${post.updatedAt}</span>
					</c:if>
				</div>
				<div class="Btn">
					<div class="icon-Btn">
						<i class="fa-regular fa-heart like-btn" data-post-no="${post.no}"></i> <i
							class="fa-regular fa-message comment-btn" onclick="toggleComments(${post.no})"
							data-post-no="${post.no}"></i>
					</div>
					<div class="update-Btn">
						<c:if test="${isAuthor}">
							<button type="button"
								onclick="window.location.href='${pageContext.request.contextPath}/updateForm.post?postNo=${post.no}'">수정</button>
							<button type="button" onclick="confirmDelete(${post.no})">삭제</button>
						</c:if>
					</div>
				</div>
			</div>
			<div id="comments" style="display: none;">
				<c:forEach var="comment" items="${comments}">
					<!-- 댓글 내용 -->
					<div class="comment" data-comment-no="${comment.no}">
						<div class="comment-header">
							<img src="${comment.profileUrl}" alt="${comment.username}" class="profile-img"> <span
								class="username">${comment.username}</span> <span class="comment-date">${comment.createdAt}</span>
						</div>
						<div class="comment-content">
							<p>${comment.content}</p>
								<button class="btn btn-sm btn-reply" id="reply-btn-${comment.no}"
									onclick="toggleReplyForm(${comment.no})">답글</button>
							<c:if test="${sessionScope.userNo == comment.userNo}">
								<button class="btn btn-sm btn-edit" onclick="showEditForm(${comment.no}, false)">수정</button>
								<button class="btn btn-sm btn-delete" onclick="deleteComment(${comment.no}, ${post.no}, false)">삭제</button>
							</c:if>
							<c:if test="${not empty comment.replies}">
								<div class="replies">
									<c:forEach var="reply" items="${comment.replies}">
										<div class="reply">
											<div class="reply-header">
												<img src="${reply.profileUrl}" alt="${reply.username}" class="profile-img"> <span
													class="username">${reply.username}</span> <span class="reply-date">${reply.createdAt}</span>
											</div>
											<div class="reply-content">
												<p>${reply.content}</p>
												<c:if test="${sessionScope.userNo == reply.userNo}">
													<button class="btn btn-sm btn-edit" onclick="showEditForm(${reply.no}, true)">수정</button>
													<button class="btn btn-sm btn-delete" onclick="deleteComment(${reply.no}, ${post.no}, true)">삭제</button>
												</c:if>
											</div>
											<div class="edit-form" id="edit-form-reply-${reply.no}" style="display: none;">
												<form action="${pageContext.request.contextPath}/updateComment.comment" method="post">
													<input type="hidden" name="commentNo" value="${reply.no}"> <input type="hidden"
														name="postNo" value="${post.no}">
													<textarea name="content" rows="2" required>${reply.content}</textarea>
													<button type="submit" class="btn btn-primary">수정</button>
													<button type="button" class="btn btn-secondary"
														onclick="hideEditForm(${reply.no}, true)">취소</button>
												</form>
											</div>
										</div>
									</c:forEach>
								</div>
							</c:if>
						</div>
						<div class="edit-form" id="edit-form-${comment.no}" style="display: none;">
							<form action="${pageContext.request.contextPath}/updateComment.comment" method="post">
								<input type="hidden" name="commentNo" value="${comment.no}"> <input type="hidden"
									name="postNo" value="${post.no}">
								<textarea name="content" rows="2" required>${comment.content}</textarea>
								<button type="submit" class="btn btn-primary">수정</button>
								<button type="button" class="btn btn-secondary" onclick="hideEditForm(${comment.no}, false)">취소</button>
							</form>
						</div>
						<!-- 대댓글 작성 폼 -->
						<div class="reply-form" id="reply-form-${comment.no}" style="display: none;">
							<form action="${pageContext.request.contextPath}/addComment.comment" method="post">
								<input type="hidden" name="postNo" value="${post.no}"> <input type="hidden"
									name="parentNo" value="${comment.no}"> <input type="hidden" name="userNo"
									value="${sessionScope.userNo}">
								<textarea name="content" rows="2" placeholder="대댓글을 작성하세요..." required></textarea>
								<button type="submit" class="btn btn-primary">답글 작성</button>
							</form>
						</div>
					</div>
				</c:forEach>
				<!-- 댓글 목록 끝 -->
				<!-- 메인 댓글 작성 시작 -->
				<form id="main-comment-form" action="${pageContext.request.contextPath}/addComment.comment"
					method="post">
					<input type="hidden" name="postNo" value="${post.no}"> <input type="hidden"
						name="userNo" value="${sessionScope.userNo}">
					<textarea name="content" rows="3" placeholder="댓글을 작성하세요..." required></textarea>
					<button type="submit" class="btn btn-primary">댓글 작성</button>
				</form>
				<!-- 메인 댓글 작성 끝 -->
			</div>
		</div>
		<div class="scroll-to-top" onclick="scrollToTop()">
			<i class="fa-solid fa-arrow-up"></i>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- jQuery 먼저 로드 -->
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"></script>
	<!-- Bootstrap 다음에 로드 -->
	<script>
    var contextPath = '${pageContext.request.contextPath}';
</script>
	<script src="<c:url value='/JS/CommunityJs/CviewFunction.js'/>"></script>
</body>
</html>
