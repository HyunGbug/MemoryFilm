
function togglePasswordVisibility() {
	const passwordInput = document.getElementById('password');
	const passwordIcon = document.getElementById('passwordIcon');
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