$(document).ready(function() {
//스크롤이 위쪽에 있으면 버튼 서서히 사락지기
	$(window).scroll(function() {
		if ($(this).scrollTop() > 200) {
			$('.scroll-to-top').fadeIn();
		} else {
			$('.scroll-to-top').fadeOut();
		}
	});
	
	// 페이지 로드 시 스크롤 위치 복원
	const scrollPosition = localStorage.getItem('scrollPosition');
	if (scrollPosition) {
		window.scrollTo(0, parseInt(scrollPosition));
		localStorage.removeItem('scrollPosition');
	}

	// 스크롤 위치 저장
	$('.btn').on('click', function() {
		localStorage.setItem('scrollPosition', window.scrollY);
	});
	// 스크롤 위치 저장 함수
	window.saveScrollPosition = function() {
		localStorage.setItem('scrollPosition', window.scrollY);
	}


	// 댓글 수정 폼 보이기/숨기기
	window.showEditForm = function(commentNo) {
		const editForm = document.getElementById(`edit-form-${commentNo}`);
		if (editForm) {
			editForm.style.display = 'block';
		}
	};

	window.hideEditForm = function(commentNo) {
		const editForm = document.getElementById(`edit-form-${commentNo}`);
		if (editForm) {
			editForm.style.display = 'none';
		}
	};

	// 댓글 삭제 확인
	window.deleteComment = function(commentNo, postNo) {
		if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
			localStorage.setItem('scrollPosition', window.scrollY);
			window.location.href = `${contextPath}/deleteCommentMyPage.comment?commentNo=${commentNo}&postNo=${postNo}`;
		}
	};

	// 댓글 수정 폼 제출 시 스크롤 위치 저장
	$('.edit-form form').on('submit', function() {
		localStorage.setItem('scrollPosition', window.scrollY);
	});
});
// 제일 상단으로 올리기 - 아이콘
function scrollToTop() {
	window.scrollTo({ top: 0 });
}