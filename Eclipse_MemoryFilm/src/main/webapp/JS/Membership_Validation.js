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

	// 비동기 요청을 콜백 함수로 처리하는 예제 > check-id엔드포인트에 get요청을 보내 요처어 url에 아이디를 쿼리 파라미터로 포함시킨다.
	function checkDuplicateId(id, callback) {
		fetch(`${contextPath}/check-id.user?id=${encodeURIComponent(id)}`) //fetch API사용해 check-id.do를 Controller로 보내 db와 검증
			.then(response => response.json())
			.then(data => {
				console.log('아이디 중복 체크 응답:', data); // 디버깅 메시지 추가
				if (data.isDuplicate) {
					alert('해당 아이디는 이미 존재하므로, 다른 아이디를 입력해주세요.');
					callback(false);
				} else {
					alert('사용 가능한 아이디 입니다.');
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

	// 닉네임 검증 - 한글은 한글 한 글자가 문자로 간주되기 때문에 정규식에 포함 x
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

	function checkDuplicateUsername(username, callback) {
		fetch(`${contextPath}/check-username.user?username=${encodeURIComponent(username)}`)//fetch API 이용
			.then(response => response.json())
			.then(data => {
				console.log('닉네임 중복 체크 응답:', data); // 디버깅 메시지 추가
				if (data.isDuplicate) {
					alert('해당 닉네임은 이미 존재하므로, 다른 닉네임을 입력해주세요.');
					callback(false);
				} else {
					alert('사용 가능한 닉네임 입니다.');
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

	function checkDuplicateEmail(email, callback) {
		fetch(`${contextPath}/check-email.user?email=${encodeURIComponent(email)}`)
			.then(response => response.json())
			.then(data => {
				console.log('이메일 중복 체크 응답:', data); // 디버깅 메시지 추가
				if (data.isDuplicate) {
					alert('해당 이메일은 이미 존재하므로, 다른 이메일을 입력해주세요.');
					callback(false);
				} else {
					alert('사용 가능한 이메일 입니다.');
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
			return false; // 회원가입을 막는다.
		}
		return true; // 회원가입을 진행
	}


	// 폼 제출 시 validateForm 호출
	document.getElementById('form').addEventListener('submit', function(event) {
		event.preventDefault();
		console.log("회원가입 폼 제출 이벤트 발생");
		validateForm(function(isValid) {
			console.log("폼 검증 결과:", isValid);
			if (isValid) {
				const formData = new FormData(event.target);

				fetch(event.target.action, {
					method: 'POST',
					body: formData
				})
					.then(response => response.json())
					.then(data => {
						if (data.success) {
							alert(data.message);
							location.href = contextPath + '/Html/LoginForm_UI.jsp';
						} else {
							alert('회원가입에 실패했습니다. 다시 시도해주세요.');
						}
					})
					.catch(error => {
						console.error('Error:', error);
						alert('회원가입에 실패했습니다. 다시 시도해주세요.');
					});
			}
		});
	});

	// 아이디, 닉네임, 비밀번호 모두 검증 완료해야지만 회원가입 완료
	function validateForm(callback) {
		if (!idValid) {
			alert('아이디를 확인해주세요.');
			callback(false);
			return;
		}
		if (!usernameValid) {
			alert('닉네임을 확인해주세요.');
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

