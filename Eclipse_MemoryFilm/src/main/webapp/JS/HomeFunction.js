var currentPhotoNo;
var photoList = [];
let editMode = false; // 페이지 로드 시 editMode 초기화

//사용자가 업로드 한 사진 개수
$(document).ready(function() {
	updatePhotoCountFromServer();
	
	//스크롤이 위쪽에 있으면 버튼 서서히 사락지기
	$(window).scroll(function() {
		if ($(this).scrollTop() > 200) {
			$('.scroll-to-top').fadeIn();
		} else {
			$('.scroll-to-top').fadeOut();
		}
	});
});

// 서버에서 사진 개수 업데이트 및 빈 날짜 제거
function updatePhotoCountFromServer() {
	$.ajax({
		url: `${contextPath}/photoCount.photo`,
		method: 'GET',
		success: function(data) {
			updatePhotoCount(data.photoCount);
		},
		error: function() {
			console.error('사진 개수를 업데이트하는 중 오류가 발생했습니다.');
		}
	});
}

//리로드 되지않는 페이지들은 업로드 한 사진 개수 이 메서드로 처리 및 사진 날짜에 사진 없으면 날짜 삭제
function updatePhotoCount(photoCount) {
	$('#photoCount').text('Total Photos: ' + (photoCount !== undefined ? photoCount : 0));
	$('.upload-date').each(function() {
		if ($(this).next('.photo-card').length === 0) {
			$(this).remove();
		}
	});
}

//메모장 수정해서 저장 json으로
document.getElementById("noteForm").addEventListener("submit", function(event) {
	event.preventDefault(); // 폼의 기본 제출 동작을 막음
	var form = event.target;
	var formData = new FormData(form);

	fetch(`${contextPath}/saveNote.user`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.json();
		})
		.then(data => {
			if (data.success) {
				showAlert('저장되었습니다.', 'alert-success');
			} else {
				showAlert('저장에 실패했습니다.', 'alert-danger');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			showAlert('오류가 발생했습니다.', 'alert-danger');
		});
});

//메모장 저장 되었습니다 알림 문구
function showAlert(message, type) {
	var alertContainer = document.getElementById("alert-container");
	var alert = document.createElement("div");
	alert.className = `memoAlert ${type}`;
	alert.textContent = message;

	alertContainer.appendChild(alert);

	// Show the alert with a fade-in effect
	setTimeout(() => {
		alert.classList.add("show");
	}, 100);

	// Hide the alert after 5 seconds with a fade-out effect
	setTimeout(() => {
		alert.classList.remove("show");
		setTimeout(() => {
			alert.remove();
		}, 500); // Wait for the fade-out transition to complete before removing the element
	}, 5000);
}

//사진 업로드 > 영상 검증
document.getElementById("upload").addEventListener("change", function() {
	var files = event.target.files;
	var maxSize = 50 * 1024 * 1024; // 50MB
	var totalSize = 0;

	for (var i = 0; i < files.length; i++) {
		if (files[i].size > maxSize) {
			alert("파일 크기가 너무 큽니다. 50MB 이하의 사진 파일만 업로드 가능합니다.");
			event.target.value = ""; // 파일 입력 초기화
			return;
		}
		totalSize += files[i].size;
	}
	if (totalSize > maxSize) {
		alert("전체 파일 크기가 50MB를 초과합니다. 파일 크기를 줄여주세요.");
		event.target.value = ""; // 파일 입력 초기화
		return;
	}
	document.getElementById("uploadForm").submit();
});

//로드될때 스크롤 위치를 기억하고, 다시 로드될 때 해당 위치로 복원
window.addEventListener("load", function() {
	const scrollPosition = localStorage.getItem("scrollPosition");
	if (scrollPosition) {
		window.scrollTo(0, parseInt(scrollPosition));
	}
});

window.addEventListener("beforeunload", function() {
	localStorage.setItem("scrollPosition", window.scrollY);
});

//제일 상단으로 올리기 - 아이콘
function scrollToTop() {
	window.scrollTo({ top: 0 });
}


//다운로드 시키기 - 아이콘
function downloadPhoto(photoNo) {
	window.location.href = contextPath + `/download.photo?no=${photoNo}`;
}

//삭제 시키기 - 아이콘
function deletePhoto(photoNo) {
	$.ajax({
		url: `${contextPath}/delete.photo`,
		method: 'POST',
		data: { no: photoNo },
		success: function(data) {
			if (data.success) {
				$(`[data-photo-no="${photoNo}"]`).remove();
				 updatePhotoCountFromServer();
				alert('사진이 삭제되었습니다.');
			} else {
				alert('사진 삭제에 실패했습니다: ' + data.message);
			}
		},
		error: function() {
			alert('사진 삭제 중 오류가 발생했습니다.');
		}
	});
}


// 사진 정보를 로드하는 함수
function loadPhotoDetails(photoNo) {
	fetch(`${contextPath}/view.photo?no=${photoNo}`)
		.then(response => response.json())
		.then(data => {
			document.getElementById("photoUploadDate").value = data.uploadDate;
			document.getElementById("photoCamera").value = data.cameraInfo;
			document.getElementById("photoFilm").value = data.lensInfo;
			document.getElementById("photoLocation").value = data.locationInfo;
			document.getElementById("photoMemo").value = data.memo;
		})
		.catch(error => console.error('Error:', error));
}

//사진 누르면 모달 창 보이게
function openModal(photoNo) {
	console.log(`openModal called with photoNo: ${photoNo}`); // 호출 확인용 로그
	if (editMode || isNaN(photoNo)) return; // 편집 모드이거나 photoNo가 유효하지 않을 때는 모달을 열지 않음

	currentPhotoNo = photoNo; // 초기 사진 번호 설정
	photoList = Array.from(document.querySelectorAll('.photo-card')).map(card => {
		return {
			no: parseInt(card.getAttribute('data-photo-no')),
			src: card.querySelector('img').src
		};
	});

	var carouselInner = document.querySelector('#photoCarousel .carousel-inner');
	carouselInner.innerHTML = '';// carousel-inner 초기화

	photoList.forEach((photo, index) => {
		var carouselItem = document.createElement('div');
		carouselItem.className = 'carousel-item' + (photo.no === photoNo ? ' active' : '');
		carouselItem.innerHTML = `<img src="${photo.src}" class="d-block w-100" alt="...">`;
		carouselItem.setAttribute('data-photo-no', photo.no);
		carouselInner.appendChild(carouselItem);
	});

	loadPhotoDetails(photoNo); // 현재 사진의 상세 정보 로드
	var myModal = new bootstrap.Modal(document.getElementById('photoModal'), {});
	myModal.show();
}

// 모달 창이 닫힐 때 백드롭 제거
document.getElementById('photoModal').addEventListener('hidden.bs.modal', function() {
	document.body.classList.remove('modal-open');
	const backdrop = document.querySelector('.modal-backdrop');
	if (backdrop) {
		backdrop.remove();
	}
});

// Carousel 이벤트 리스너 추가
document.getElementById('photoCarousel').addEventListener('slid.bs.carousel', function(event) {
	console.log('크라우셀 메서드 호출');
	var activeItem = event.target.querySelector('.carousel-item.active');
	var photoNo = parseInt(activeItem.getAttribute('data-photo-no'));

	currentPhotoNo = photoNo; // 슬라이드 변경 시 현재 사진 번호 업데이트
	loadPhotoDetails(photoNo); // 새로운 사진 정보 로드
});

// 모달 데이터 저장
document.getElementById("photoInfoForm").addEventListener("submit", function(event) {
	event.preventDefault();
	var form = event.target;
	var formData = new FormData(form);
	formData.append('photoNo', currentPhotoNo); // 현재 사진 번호 추가

	fetch(`${contextPath}/savePhotoDetails.photo`, {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			return response.text(); // 응답을 먼저 텍스트로 받음
		})
		.then(data => {
			try {
				const jsonData = JSON.parse(data); // 텍스트를 JSON으로 파싱
				if (jsonData.success) {
					alert('저장되었습니다.');
				} else {
					alert('저장에 실패했습니다.');
				}
			} catch (error) {
				console.error('JSON parsing error:', error);
				 console.error('Original response:', data); // 원본 응답을 로그로 남김
			}
		})
		.catch(error => console.error('Error:', error));
});

//다중 선택해서 삭제 및 다운로드
document.addEventListener("DOMContentLoaded", function() {
	const editButton = document.getElementById('editButton');
	const deleteSelectedButton = document.getElementById('deleteSelectedButton');
	const downloadSelectedButton = document.getElementById('downloadSelectedButton');
	const instructionText = document.getElementById('instructionText');
	const photoCards = document.querySelectorAll('.photo-card');
	const photoImages = document.querySelectorAll('.photo-img');

	//편집 모드
	editButton.addEventListener('click', function() {
		editMode = !editMode; //반대로 변경

		// 편집 모드일 때는 photoCards에 'editable' 클래스를 추가, 아닐 때는 제거
		photoCards.forEach(card => {
			if (editMode) {
				card.classList.add('editable');
			} else {
				card.classList.remove('editable', 'selected');
			}
		});
		//모드에 따라 버튼 텍스트 변경하기
		editButton.textContent = editMode ? '선택 닫기' : '이미지 선택';

		//삭제 및 다운로드 버튼 표시/숨기기
		deleteSelectedButton.style.display = editMode ? 'inline-block' : 'none';
		downloadSelectedButton.style.display = editMode ? 'inline-block' : 'none';


		// 수정 기능 사용 방법 문구 표시/숨기기
		instructionText.style.display = editMode ? 'block' : 'none';
	});

	//편집모드일 때 모달 창 띄우지 않기 <> 아니면 모달 창 띄우기
	photoImages.forEach(img => {
		img.addEventListener('click', function(event) {
			if (editMode) {
				event.stopPropagation(); // 모달을 여는 기본 동작 중지
				const photoCard = img.closest('.photo-card');
				photoCard.classList.toggle('selected'); // 선택된 사진 토글
			} else {
				const photoNo = parseInt(img.closest('.photo-card').getAttribute('data-photo-no'));
				openModal(photoNo);

			}
		});
	});

	//편집 모드의 삭제 버튼 눌렀을 때
	deleteSelectedButton.addEventListener('click', function() {
		const selectedPhotos = document.querySelectorAll('.photo-card.selected');
		const photoNos = Array.from(selectedPhotos).map(card => card.getAttribute('data-photo-no'));
		if (photoNos.length > 0) {
			if (confirm("해당 사진들을 모두 삭제하시겠습니까?")) {

				$.ajax({
					url: `${contextPath}/deleteSelectedPhotos.photo`,
					method: 'POST',
					data: { photoNos: photoNos },  //배열로 전송
					traditional: true, //배열 데이터를 올바르게 전송하기 위해
					success: function(response) {
						if (response.success) {
							selectedPhotos.forEach(card => card.remove());
							 updatePhotoCountFromServer();
							alert("삭제가 완료되었습니다.");
						} else {
							alert("삭제를 실패하였습니다.");
						}
					},
					error: function(error) {
						console.error('Error:', error);
						alert("사진 삭제 중 오류가 발생했습니다.");
					}
				});
			}
		} else {
			alert("선택된 사진이 없습니다. 사진을 선택해주세요.");
		}
	});


	//zip으로 다운로드
	downloadSelectedButton.addEventListener('click', function() {
		const selectedPhotos = document.querySelectorAll('.photo-card.selected');
		const photoNos = Array.from(selectedPhotos).map(card => card.getAttribute('data-photo-no'));
		if (photoNos.length > 0) {
			const form = document.createElement('form');
			form.method = 'POST';
			form.action = `${contextPath}/downloadSelectedPhotos.photo`;
			photoNos.forEach(no => {
				const input = document.createElement('input');
				input.type = 'hidden';
				input.name = 'photoNos';
				input.value = no;
				form.appendChild(input);
			});
			document.body.appendChild(form);
			form.submit();
			document.body.removeChild(form);
		} else {
			alert("선택된 사진이 없습니다. 사진을 선택해주세요.");
		}
	});
});
