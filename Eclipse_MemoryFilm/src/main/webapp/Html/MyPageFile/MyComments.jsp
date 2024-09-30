<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: My Page</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/MyPage/MyCommentsStyle.css' />">
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
		<div class="comments-container">
			<c:choose>
				<c:when test="${empty comments}">
					<h2>작성한 댓글이 없습니다.</h2>
				</c:when>
				<c:otherwise>
					<c:forEach var="comment" items="${comments}">
						<div class="comment-card">
							<div class="comment-header">
								<img src="${comment.profileUrl}" alt="${comment.username}" class="profile-img"> <span
									class="username">${comment.username}</span> <span class="comment-date">${comment.createdAt}</span>
							</div>
							<div class="comment-content">
								<p>${comment.content}</p>
							</div>
							<div class="comment-actions">
								<button type="button" class="btn btn-sm btn-edit"  onclick="showEditForm(${comment.no})">수정</button>
								<button type="button" class="btn btn-sm btn-delete"
									onclick="deleteComment(${comment.no}, ${comment.postNo})">삭제</button>
							</div>
							<div class="edit-form" id="edit-form-${comment.no}" style="display: none;">
								<form action="${pageContext.request.contextPath}/updateCommentMyPage.comment" method="post"
									onsubmit="saveScrollPosition()">
									<input type="hidden" name="commentNo" value="${comment.no}"> <input type="hidden"
										name="postNo" value="${comment.postNo}">
									<textarea name="content" rows="2" required>${comment.content}</textarea>
									<button type="submit" class="btn btn-primary">수정</button>
									<button type="button" class="btn btn-secondary" onclick="hideEditForm(${comment.no})">취소</button>
								</form>
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
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"></script>
	<script>
    var contextPath = '${pageContext.request.contextPath}';
</script>
	<script src="<c:url value='/JS/MyPage/MyCommentsFunction.js' />"></script>
</body>
</html>
