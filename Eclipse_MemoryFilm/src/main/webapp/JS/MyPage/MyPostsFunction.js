$(document).ready(function() {
	 //스크롤
    $('#communityButton').on('click', function() {
        sessionStorage.setItem('scrollPosition', window.scrollY);
    });
	 
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
	 
	 // 좋아요 상태 요청
    $.get(contextPath + '/getLiked.post', function(response) {
        response.forEach(function(postNo) {
            $('.like-btn[data-post-no="' + postNo + '"]').removeClass('fa-regular').addClass('fa-solid');
        });
    }, 'json').fail(function(jqXHR) {
        console.error("좋아요 상태 요청 실패: ", jqXHR);  // 에러 로그 추가

        if (jqXHR.status === 401) {
            alert("로그인이 필요합니다.");
            window.location.href = contextPath + '/Html/LoginForm.jsp';
        } else {
            alert("좋아요 상태를 불러오는 중 오류가 발생했습니다.");
        }
    });

    // 좋아요 버튼 클릭 이벤트
    $('.like-btn').click(function() {
        const postNo = $(this).data('post-no');
        const likeBtn = $(this);
        $.post(contextPath + '/toggleLike.post', { postNo: postNo }, function(response) {
            if (response.liked) {
                likeBtn.removeClass('fa-regular').addClass('fa-solid');
            } else {
                likeBtn.removeClass('fa-solid').addClass('fa-regular');
            }
        }, 'json').fail(function(jqXHR) {
            console.log("좋아요 상태 변경 실패: ", jqXHR);  // 디버깅용 로그
            if (jqXHR.status === 401) {
                alert("로그인이 필요합니다.");
                window.location.href = contextPath + '/Html/LoginForm.jsp';
            } else {
                alert("좋아요 상태를 변경하는 중 오류가 발생했습니다.");
            }
        });
    });
	});
	// 제일 상단으로 올라가는 버튼
	function scrollToTop() {
		$('html, body').animate({ scrollTop: 0 }, 'slow');
	}
        function confirmDelete(postNo) {
            if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
                window.location.href = contextPath + '/myPostsDelete.post?postNo=' + postNo;
            }
        }
        function updatePost(postNo){
        	localStorage.setItem('scrollPosition', window.scrollY);
        	window.location.href = contextPath + '/myPostsUpdateForm.post?postNo=' + postNo;
        }