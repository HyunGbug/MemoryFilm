<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Error</title>
<style>
.error-container {
	text-align: center;
	padding: 50px;
}

.error-container h1 {
	color: #e74c3c;
}

.error-container p {
	font-size: 18px;
	color: #555;
}

.error-container button {
	margin-top: 20px;
	padding: 10px 20px;
	background-color: #3498db;
	color: #fff;
	border: none;
	border-radius: 5px;
	cursor: pointer;
}

.error-container button:hover {
	background-color: #2980b9;
}
</style>
</head>
<body>
	<div class="error-container">
		<h1>오류가 발생했습니다</h1>
		<p>
			<c:out value="${error}" />
		</p>
		<button onclick="window.history.back();">뒤로가기</button>
	</div>
</body>
</html>
