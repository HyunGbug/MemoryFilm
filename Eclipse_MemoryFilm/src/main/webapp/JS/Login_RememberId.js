// js/Login_Validation.js
function showError(message){
    alert(message);
}

// 쿠키에서 값 읽기
window.onload = function() { //브라우저 창이 완전히 로드된 후 실행 될 함수 정의
    var rememberUserId = getCookie("rememberUserId");
    
    if (rememberUserId) {//위 쿠키 값이 존재한다면, 해당 값을 id 입력 필드에 설정하고 아이디 기억하기를 체크된 상태로 설정
		
        document.getElementById('id').value = rememberUserId;
        document.getElementById('rememberMe').checked = true;
    }
}

function getCookie(name) {//쿠키에서 특정 이름의 값을 가져오는 함수
    var cookieArr = document.cookie.split(";"); //현재 도메인에 설정된 모든 쿠키를 문자열로 반환 > 세미콜론으로 구분
    
    for(var i = 0; i < cookieArr.length; i++) {
		
        var cookiePair = cookieArr[i].split("="); //각 쿠키를 이름과 값으로 분할
        
        if(name == cookiePair[0].trim()) {//현재 찾고자 하는 쿠키 이름과 일치하는지 확인
			
            return decodeURIComponent(cookiePair[1]); //일치하면 해당 쿠키의 값 반환
        }
    }
    return null; //해당 쿠키 없으면 null 반환
}
