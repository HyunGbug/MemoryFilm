<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Memory Film :: Home</title>
<!-- 부트스트랩 -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<!-- 아이콘 -->
<script src="https://kit.fontawesome.com/3b644b33f6.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomeStyle.css' />">
<link rel="stylesheet" type="text/css" href="<c:url value='/Css/Home/HomePageStyle.css' />">
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
		<div class="profile-card">
			<div id="photoCount" class="photoCnt"></div>
			<img src="<c:out value='${sessionScope.user.profileUrl}'/>" alt="Profile">
			<h3>
				<c:out value='${sessionScope.user.userName}' />
			</h3>
			<form id="noteForm">
				<textarea name="note" placeholder="메모장" cols="25" rows="15"><c:out
						value="${sessionScope.user.memo}" /></textarea>
				<button type="submit" class="memoSubmitBtn">저장</button>
				<div id="alert-container"></div>
				<!-- 알림 컨테이너 -->
			</form>
		</div>
		<!-- 사진 업로드 -->
		<div class="upload-section">
			<form id="uploadForm" action="${pageContext.request.contextPath}/upload.photo" method="post"
				enctype="multipart/form-data">
				<label for="upload" class="upload-label"> <i class="fa-solid fa-plus"></i> Upload Images
				</label> <input type="file" id="upload" name="images" multiple>
			</form>
			<div class="edit-section">
				<button type="button" id="editButton" class="editBtn btn-secondary">이미지 선택</button>
				<button type="button" id="deleteSelectedButton" class="eBtn btn-danger" style="display: none;">
					<i class="fa-solid fa-trash-can delete-icon"></i> Delete
				</button>
				<button type="button" id="downloadSelectedButton" class="eBtn btn-primary"
					style="display: none;">
					<i class="fa-solid fa-cloud-arrow-down download-icon"></i> Download
				</button>
			</div>
			<p id="instructionText" class="instructionText" style="display: none;">
				이미지를 선택해서 원하시는 기능의 버튼을 선택하세요. <br>종료하시려면 선택 닫기를 눌러주세요.
			</p>
			<div class="photo-gallery">
				<c:forEach var="entry" items="${sessionScope.groupedPhotos}">
					<div class="upload-date"  data-upload-date="${entry.key}">${entry.key}</div>
					<c:forEach var="photo" items="${entry.value}">
						<div class="photo-card" data-photo-no="${photo.no}" data-upload-date="${entry.key}">
							<img src="${pageContext.request.contextPath}/${photo.filePath}" class="photo-img" alt="Photo">
							<div class="photo-actions">
								<i class="fa-solid fa-cloud-arrow-down download-icon" onclick="downloadPhoto(${photo.no})"></i>
								<i class="fa-solid fa-trash-can delete-icon" onclick="deletePhoto(${photo.no})"></i>
							</div>
						</div>
					</c:forEach>
				</c:forEach>
			</div>
		</div>
		<div class="scroll-to-top" onclick="scrollToTop()">
			<i class="fa-solid fa-arrow-up"></i>
		</div>
	</main>
	<jsp:include page="/Common_Jsp/footer.jsp" />
	<!-- Modal -->
	<div class="modal fade" id="photoModal" data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="photoModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="photoModalLabel">사진 정보 작성하기</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div id="photoCarousel" class="carousel slide" data-bs-interval="false">
						<div class="carousel-inner">
							<!-- 모달 창 사진 띄워지는 곳 -->
						</div>
					</div>
					<form id="photoInfoForm">
						<div class="mb-3">
							<label for="photoUploadDate" class="form-label">Upload Date</label> <input type="text"
								class="form-control" id="photoUploadDate" name="uploadDate" readonly>
						</div>
						<div class="mb-3">
							<label for="photoCamera" class="form-label">Camera</label> <input type="text"
								class="form-control" id="photoCamera" name="camera" placeholder="카메라 모델을 입력하세요.">
						</div>
						<div class="mb-3">
							<label for="photoFilm" class="form-label">Film</label> <input type="text"
								class="form-control" id="photoFilm" name="film" placeholder="필름 종류를 입력하세요.">
						</div>
						<div class="mb-3">
							<label for="photoLocation" class="form-label">Location</label> <input type="text"
								class="form-control" id="photoLocation" name="location" placeholder="촬영 장소를 입력하세요.">
						</div>
						<div class="mb-3">
							<label for="photoMemo" class="form-label">Memo</label>
							<textarea class="form-control" id="photoMemo" name="memo" rows="5" placeholder="메모를 입력하세요."
								style="resize: none;"></textarea>
						</div>
						<button type="submit" class="btn btn-primary">저장</button>
					</form>
				</div>
				<div class="modal-footer">
					<button class="btn btn-secondary carousel-control-prev" type="button"
						data-bs-target="#photoCarousel" data-bs-slide="prev">
						<span class="carousel-control-prev-icon" aria-hidden="true"></span> <span
							class="visually-hidden">Previous</span>
					</button>
					<button class="btn btn-secondary carousel-control-next" type="button"
						data-bs-target="#photoCarousel" data-bs-slide="next">
						<span class="carousel-control-next-icon" aria-hidden="true"></span> <span
							class="visually-hidden">Next</span>
					</button>
				</div>
			</div>
		</div>
	</div>
	<script src="<c:url value='/JS/NavBar_Color.js' />"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		var contextPath = '<%= request.getContextPath() %>';
	</script>
	<script src="<c:url value='/JS/HomeFunction.js' />"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.7/dist/umd/popper.min.js"
		></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
		></script>
</body>
</html>
