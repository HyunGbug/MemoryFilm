let currentOpenForm = null;
let currentOpenButton = null;

$(document).ready(function() {
    $('[id^="carouselExampleIndicators"]').carousel({
        interval: false
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
    const scrollPosition = sessionStorage.getItem('scrollPosition');
    if (scrollPosition) {
        window.scrollTo(0, parseInt(scrollPosition));
        sessionStorage.removeItem('scrollPosition');
    }

    // 스크롤
    $('#communityButton').on('click', function() {
        sessionStorage.setItem('scrollPosition', window.scrollY);
    });

    // 좋아요 상태 요청
    $.get(contextPath + '/getLiked.post', function(response) {
        response.forEach(function(postNo) {
            $('.like-btn[data-post-no="' + postNo + '"]').removeClass('fa-regular').addClass('fa-solid');
        });
    }, 'json').fail(function(jqXHR) {
        console.error("좋아요 상태 요청 실패: ", jqXHR);

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
            console.log("좋아요 상태 변경 실패: ", jqXHR);
            if (jqXHR.status === 401) {
                alert("로그인이 필요합니다.");
                window.location.href = contextPath + '/Html/LoginForm.jsp';
            } else {
                alert("좋아요 상태를 변경하는 중 오류가 발생했습니다.");
            }
        });
    });

    // 쿼리 파라미터 확인
    var urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('showComments') === 'true') {
        var postNo = urlParams.get('postNo');
        toggleComments(postNo);
    }
});

function confirmDelete(postNo) {
    if (confirm("정말로 이 게시글을 삭제하시겠습니까?")) {
        window.location.href = contextPath + '/delete.post?postNo=' + postNo;
    }
}

// 댓글
function toggleComments(postNo) {
    var commentsDiv = document.getElementById('comments');
    if (commentsDiv.style.display === 'none') {
        $.ajax({
            url: contextPath + '/viewComments.comment',
            type: 'GET',
            data: { postNo: postNo },
            success: function(data) {
                $('#comments').html($(data).find('#comments').html());
                commentsDiv.style.display = 'block';
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error('Error loading comments:', textStatus, errorThrown, jqXHR.responseText);
                alert('댓글을 불러오는 중 오류가 발생했습니다.');
            }
        });
    } else {
        commentsDiv.style.display = 'none';
    }
}

function showEditForm(commentNo, isReply) {
    // 기존에 열린 폼이 있으면 닫기
    if (currentOpenForm) {
        currentOpenForm.style.display = 'none';
        if (currentOpenButton) {
            currentOpenButton.textContent = currentOpenButton.getAttribute('data-original-text');
        }
    }

    var formId = isReply ? 'edit-form-reply-' + commentNo : 'edit-form-' + commentNo;
    var editForm = document.getElementById(formId);
    var editBtn = isReply ? document.querySelector('.btn-edit-reply[data-comment-no="' + commentNo + '"]') : document.querySelector('.btn-edit[data-comment-no="' + commentNo + '"]');

    if (editForm) {
        editForm.style.display = 'block';
        if (editBtn) {
            editBtn.setAttribute('data-original-text', editBtn.textContent);
            editBtn.textContent = '수정 닫기';
        }
        document.getElementById('main-comment-form').style.display = 'none';
        currentOpenForm = editForm;
        currentOpenButton = editBtn;
    } else {
        console.error('Edit form not found for commentNo:', commentNo);
    }
}

// 대댓글
function toggleReplyForm(commentNo) {
    // 기존에 열린 폼이 있으면 닫기
    if (currentOpenForm) {
        currentOpenForm.style.display = 'none';
        if (currentOpenButton) {
            currentOpenButton.textContent = currentOpenButton.getAttribute('data-original-text');
        }
    }

    var replyForm = document.getElementById('reply-form-' + commentNo);
    var replyBtn = document.getElementById('reply-btn-' + commentNo);

    if (replyForm.style.display === 'none') {
        replyForm.style.display = 'block';
        if (replyBtn) {
            replyBtn.setAttribute('data-original-text', replyBtn.textContent);
            replyBtn.textContent = '답글 닫기';
        }
        document.getElementById('main-comment-form').style.display = 'none';
        currentOpenForm = replyForm;
        currentOpenButton = replyBtn;
    } else {
        replyForm.style.display = 'none';
        if (replyBtn) {
            replyBtn.textContent = replyBtn.getAttribute('data-original-text');
        }
        document.getElementById('main-comment-form').style.display = 'block';
        currentOpenForm = null;
        currentOpenButton = null;
    }
}

function hideEditForm(commentNo, isReply) {
    var formId = isReply ? 'edit-form-reply-' + commentNo : 'edit-form-' + commentNo;
    var editForm = document.getElementById(formId);
    var editBtn = isReply ? document.querySelector('.btn-edit-reply[data-comment-no="' + commentNo + '"]') : document.querySelector('.btn-edit[data-comment-no="' + commentNo + '"]');

    if (editForm) {
        editForm.style.display = 'none';
        if (editBtn) {
            editBtn.textContent = editBtn.getAttribute('data-original-text');
        }
        document.getElementById('main-comment-form').style.display = 'block';
        currentOpenForm = null;
        currentOpenButton = null;
    } else {
        console.error('Edit form not found for commentNo:', commentNo);
    }
}

function deleteComment(commentNo, postNo, isReply) {
    if (confirm("정말로 이 댓글을 삭제하시겠습니까?")) {
		  localStorage.setItem('scrollPosition', window.scrollY);
        let url = contextPath + '/deleteComment.comment?commentNo=' + commentNo + '&postNo=' + postNo;
        if (isReply) {
            url += '&isReply=true';
        }
        window.location.href = url;
    }
}

function goBackToCommunity() {
    sessionStorage.setItem('scrollPosition', window.scrollY);
    window.location.href = contextPath + '/list.post';
}

// 제일 상단으로 올리기 - 아이콘
function scrollToTop() {
    window.scrollTo({ top: 0 });
}

// 댓글 작성 시 스크롤 위치 저장
$('#main-comment-form, .reply-form form').on('submit', function() {
    localStorage.setItem('scrollPosition', window.scrollY);
    document.getElementById('scrollPosition').value = window.scrollY; // 스크롤 위치 저장
});