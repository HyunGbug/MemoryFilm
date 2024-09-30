<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Community</title>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Community/CwriteStyle.css' />">
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
		<div class="create-post-container">
			<h2>게시글 작성하기</h2>
			<!-- Community로 돌아가기 버튼 추가 -->
			<button id="communityButton" class="previewBtn"><i class="fa-solid fa-list-ul"></i> 목록</button>
			<form action="${pageContext.request.contextPath}/write.post" method="post"
				enctype="multipart/form-data">
				<label for="title">Title</label> <input type="text" id="title" name="title"
					placeholder="글의 제목을 입력하세요." required> <label for="content">Content</label>
				<textarea id="content" name="content" rows="5" placeholder="글의 내용을 입력하세요." required></textarea>
				<!-- 사용자가 기존에 업로드 했던 사진 -->
				<button type="button" id="showExistingPhotosButton" class="existingPhotos">Home에서 사진
					가져오기</button>
				<div id="existingPhotoContainer" style="display: none;" class="existing-photos">
					<c:forEach var="entry" items="${sessionScope.groupedPhotos}">
						<c:forEach var="photo" items="${entry.value}">
							<div class="existing-photo">
								<img src="${pageContext.request.contextPath}/${photo.filePath}" class="selectable-photo"
									data-photo-no="${photo.no}">
							</div>
						</c:forEach>
					</c:forEach>
				</div>
				<input type="hidden" id="selectedExistingPhotos" name="selectedExistingPhotos">
				<!-- 새 사진 업로드 -->
				<label for="newPhotos" class="newPhotos-label"> <i class="fa-solid fa-plus"></i> new
					Images
				</label> <input type="file" name="newPhotos" id="newPhotos" multiple accept="image/*">
				<div class="new-photos-preview" id="newPhotosPreview"></div>
				<button type="submit">업로드</button>
			</form>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		var contextPath = '${pageContext.request.contextPath}';
	</script>
	<script src="<c:url value='/JS/CommunityJs/CwriteFunction.js' />"></script>
</body>
</html>
