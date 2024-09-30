$(document).ready(function() {
	$('#resetPasswordForm').on('submit', function(event) {
		event.preventDefault();
		$.ajax({
			type: 'POST',
			url:  contextPath + '/resetPassword.user',
			data: $(this).serialize(),
			 dataType: 'json', // 서버 응답을 JSON으로 기대합니다.
			success: function(response) {
				   if (response.success) {
                    alert(response.message);
                    window.location.href = contextPath + "/Html/LoginForm.jsp"; // 성공 시 로그인 페이지로 이동
                } else {
                    alert(response.message);
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('요청을 처리하는 동안 오류가 발생했습니다.');
                console.log('Status: ' + textStatus);
                console.log('Error: ' + errorThrown);
                console.log('Response: ' + jqXHR.responseText);
            }
		});
	});
});