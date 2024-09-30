// 검증 상태를 저장할 변수
let idValid = true; // 초기값을 true로 설정
let usernameValid = true; // 초기값을 true로 설정
let emailValid = true; // 초기값을 true로 설정

const originalId = document.getElementById('id').value;
const originalUsername = document.getElementById('username').value;
const originalEmail = document.getElementById('email').value;
const originalPassword = document.getElementById('password').value;

function addValidationEventListeners() {
    document.getElementById('checkIdBtn').addEventListener('click', function() {
        validateId();
    });

    document.getElementById('id').addEventListener('input', function() {
        idValid = (this.value === originalId);
    });

    document.getElementById('checkUsernameBtn').addEventListener('click', function() {
        validateUsername();
    });

    document.getElementById('username').addEventListener('input', function() {
        usernameValid = (this.value === originalUsername);
    });

    document.getElementById('checkEmailBtn').addEventListener('click', function() {
        validateEmail();
    });

    document.getElementById('email').addEventListener('input', function() {
        emailValid = (this.value === originalEmail);
    });

    document.getElementById('updateForm').addEventListener('submit', function(event) {
        event.preventDefault();
        console.log("본인 정보 수정 폼 제출 이벤트 발생");
        validateForm(function(isValid) {
            if (isValid) {
                submitForm(event.target);
            }
        });
    });
}

function validateId() {
    const id = document.getElementById('id').value;
    const idRegex = /^[a-zA-Z0-9_]{1,15}$/;

    if (id.length > 15) {
        alert('아이디는 15자리 이하로 입력해야 합니다.');
        idValid = false;
    } else if (!idRegex.test(id)) {
        alert('아이디는 영어, 숫자, 밑줄(_)만 사용 가능하며 15자리 이하로 입력해야 합니다.');
        idValid = false;
    } else {
        checkDuplicate('id', id, function(isValid) {
            idValid = isValid;
        });
    }
}

function validateUsername() {
    const username = document.getElementById('username').value;
    const usernameRegex = /^[a-zA-Z0-9가-힣_]+$/;

    if (!usernameRegex.test(username)) {
        alert('닉네임은 영어, 한글, 숫자, 밑줄(_)만 사용 가능합니다.');
        usernameValid = false;
    } else if ([...username].length > 8) {
        alert('닉네임은 8자리 이하로 입력해야 합니다.');
        usernameValid = false;
    } else {
        checkDuplicate('username', username, function(isValid) {
            usernameValid = isValid;
        });
    }
}

function validateEmail() {
    const email = document.getElementById('email').value;
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailRegex.test(email)) {
        alert('유효한 이메일 주소를 입력해주세요.');
        emailValid = false;
    } else {
        checkDuplicate('email', email, function(isValid) {
            emailValid = isValid;
        });
    }
}

function checkDuplicate(type, value, callback) {
    // typeNameMap은 사용자에게 보여줄 이름으로 설정
    const typeNameMap = {
        "username": "닉네임",
        "id": "아이디",
        "email": "이메일"
    };

    const typeName = typeNameMap[type] || type; // 사용자에게 보여줄 이름으로 변환

    fetch(`${contextPath}/check-update-${type}.user?${type}=${encodeURIComponent(value)}`)
        .then(response => response.json())
        .then(data => {
            if (data.isDuplicate) {
                alert(`해당 ${typeName}은/는 이미 존재하므로, 다른 ${typeName}을/를 입력해주세요.`);
                callback(false);
            } else {
                alert(`사용 가능한 ${typeName} 입니다.`);
                callback(true);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            callback(false);
        });
}

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


function validateForm(callback) {
    const idChanged = document.getElementById('id').value !== originalId;
    const usernameChanged = document.getElementById('username').value !== originalUsername;
    const emailChanged = document.getElementById('email').value !== originalEmail;
    const passwordChanged = document.getElementById('password').value !== originalPassword;

    if (idChanged && !idValid) {
        alert('아이디를 확인해주세요.');
        callback(false);
        return;
    }
    if (usernameChanged && !usernameValid) {
        alert('닉네임을 확인해주세요.');
        callback(false);
        return;
    }
    if (emailChanged && !emailValid) {
        alert('이메일을 확인해주세요.');
        callback(false);
        return;
    }
    if (passwordChanged && !validatePassword()) {
        callback(false);
        return;
    }
    callback(true);
}

function submitForm(form) {
    const formData = new FormData(form);
    fetch(form.action, {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert(data.message);
            location.reload();
        } else {
            alert(data.message);
            if (data.message === "세션이 만료되었습니다. 다시 로그인 해주세요.") {
                location.href = contextPath + '/Html/LoginForm_UI.jsp';
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('수정에 실패했습니다. 다시 시도해주세요.');
    });
}

//파일 입력 필드에서 새 파일을 선택할 떄 트리거
document.getElementById('profilePicture').addEventListener('change', function() {
    const input = document.getElementById('profilePicture'); //사용자가 선택한 사진 
    const preview = document.getElementById('profilePicturePreview').querySelector('img');
    const file = input.files[0];//선택된 파일 참조

    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
        }
        reader.readAsDataURL(file);//파일을 읽고 Data URL 형식으로 변환시켜 브라우저가 이미지를 표시할 수 있게 함
    }	//미리보기 갱신 
});

//비밀번호 아이콘으로 보이게
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

// 초기화
addValidationEventListeners();
