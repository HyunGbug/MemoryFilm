// 검증 상태를 저장할 변수
let idValid = false;
let usernameValid = false;
let emailValid = false;

//비밀번호 타입 변경
function togglePasswordVisibility(passwordId, iconId) {
	const passwordInput = document.getElementById(passwordId);
	const passwordIcon = document.getElementById(iconId);
	if (passwordInput.type === 'password') {
		passwordInput.type = 'text';
		passwordIcon.classList.remove('fa-eye-slash');
		passwordIcon.classList.add('fa-eye');
	} else {
		passwordInput.type = 'password';
		passwordIcon.classList.remove('fa-eye');
		passwordIcon.classList.add('fa-eye-slash');
	}
}

// 아이디 중복 확인 
document.getElementById('checkIdBtn').addEventListener('click', function() {
	validateId(function(isValid) {
		idValid = isValid;
	});
});

// id 검증
function validateId(callback) {
	const id = document.getElementById('id').value;
	const idRegex = /^[a-zA-Z0-9_]{1,15}$/; // 영어, 숫자, 밑줄, 15자리 이하

	if (id.length > 15) {
		alert('아이디는 15자리 이하로 입력해야 합니다.');
		callback(false);
	} else if (!idRegex.test(id)) {
		alert('아이디는 영어, 숫자, 밑줄(_)만 사용 가능하며 15자리 이하로 입력해야 합니다.');
		callback(false);
	} else {
		checkDuplicateId(id, callback);
	}
}

// 아이디 중복 확인 요청
function checkDuplicateId(id, callback) {
	fetch('/auth/check-id', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(id)
	})
		.then(response => response.text())
		.then(data => {
			console.log('아이디 중복 체크 응답:', data);
			if (data === "아이디가 이미 존재합니다.") {
				alert(data);
				callback(false);
			} else {
				alert(data);
				callback(true);
			}
		})
		.catch(error => {
			console.error('Error:', error);
			callback(false);
		});
}

//닉네임 중복 확인
document.getElementById('checkUsernameBtn').addEventListener('click', function() {
	validateUsername(function(isValid) {
		usernameValid = isValid;
	});
});

// 닉네임 검증
function validateUsername(callback) {
	const username = document.getElementById('username').value;
	const usernameRegex = /^[a-zA-Z0-9가-힣_]+$/; // 영어, 한글, 숫자, 밑줄

	if (!usernameRegex.test(username)) {
		alert('닉네임은 영어, 한글, 숫자, 밑줄(_)만 사용 가능합니다.');
		callback(false);
	} else if ([...username].length > 8) {
		alert('닉네임은 8자리 이하로 입력해야 합니다.');
		callback(false);
	} else {
		checkDuplicateUsername(username, callback);
	}
}

// 닉네임 중복 확인 요청
function checkDuplicateUsername(username, callback) {
	fetch('/auth/check-username', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(username)
	})
		.then(response => response.text())
		.then(data => {
			console.log('닉네임 중복 체크 응답:', data);
			if (data === "닉네임이 이미 존재합니다.") {
				alert(data);
				callback(false);
			} else {
				alert(data);
				callback(true);
			}
		})
		.catch(error => {
			console.error('Error:', error);
			callback(false);
		});
}

// 이메일 중복 확인
document.getElementById('checkEmailBtn').addEventListener('click', function() {
	validateEmail(function(isValid) {
		emailValid = isValid;
	});
});

// 이메일 검증
function validateEmail(callback) {
	const email = document.getElementById('email').value;
	const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

	if (!emailRegex.test(email)) {
		alert('유효한 이메일 주소를 입력해주세요.');
		callback(false);
	} else {
		checkDuplicateEmail(email, callback);
	}
}

// 이메일 중복 확인 요청
function checkDuplicateEmail(email, callback) {
	fetch('/auth/check-email', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify(email)
	})
		.then(response => response.text())
		.then(data => {
			console.log('이메일 중복 체크 응답:', data);
			if (data === "이메일이 이미 존재합니다.") {
				alert(data);
				callback(false);
			} else {
				alert(data);
				callback(true);
			}
		})
		.catch(error => {
			console.error('Error:', error);
			callback(false);
		});
}

// 비밀번호 유효성 검사
function validatePassword() {
	const password = document.getElementById('password').value;
	const passwordChk = document.getElementById('passwordChk').value;
	const passwordRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;

	if (!passwordRegex.test(password)) {
		alert('비밀번호는 8~20자리로, 문자, 숫자, 특수문자를 포함해야 합니다.');
		return false;
	}

	if (password !== passwordChk) {
		alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
		return false;
	}
	return true;
}



// 폼 제출 시 validateForm 호출
document.getElementById('form').addEventListener('submit', function(event) {
	event.preventDefault();
	validateForm(function(isValid) {
		if (isValid) {
			const formData = new FormData(event.target); // 전체 폼 데이터를 FormData로 전송

			//서버의 회워나입 API로 요청 보내기
			fetch('/auth/signup', {
				method: 'POST',
				body: formData
			})
				.then(response => response.json())
				.then(data => {
					if (data.success) {
						alert('회원가입에 성공했습니다.');
						location.href = '/login'; //회원가입 후 로그인 페이지로 이동
					} else {
						alert('회원가입에 실패했습니다. 다시 시도해주세요.');
					}
				})
				.catch(error => {
					console.error('Error:', error);
					alert('오류료 인해 회원가입에 실패했습니다. 다시 시도해주세요.');
				});
		}
	});
});

// 아이디, 닉네임, 비밀번호 모두 검증 완료해야지만 회원가입 완료
function validateForm(callback) {
	if (!idValid) {
		alert('아이디의 유효성을 체크해주세요.');
		callback(false);
		return;
	}
	if (!usernameValid) {
		alert('닉네임의 유효성을 체크해주세요.');
		callback(false);
		return;
	}
	if (!emailValid) {
		alert('이메일의 유효성을 체크해주세요.');
		callback(false);
		return;
	}
	if (!validatePassword()) {
		callback(false);
		return;
	}
	callback(true);
}




// 프로필 사진 선택하면 미리보기로 사진에 추가
function previewProfilePicture() {
	const input = document.getElementById('profilePicture');
	const preview = document.getElementById('profilePicturePreview').querySelector('img');
	const file = input.files[0];

	if (file) {
		const reader = new FileReader();
		reader.onload = function(e) {
			preview.src = e.target.result;
		}
		reader.readAsDataURL(file);
	}
}

document.getElementById('profilePicture').addEventListener('change', previewProfilePicture);
