$(document).ready(function() {
	$('#showExistingPhotosButton').click(function() {
		const photoContainer = $('#existingPhotoContainer');
		const isVisible = photoContainer.is(':visible');
		photoContainer.toggle();
		$(this).text(isVisible ? 'Home에서 사진 가져오기' : '닫기');
	});

	let selectedPhotos = [];
	let maxPhotos = 8;
	let maxSize = 50 * 1024 * 1024; // 50MB

	$('.selectable-photo').click(function() {
		const photoNo = $(this).data('photo-no');
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
			selectedPhotos = selectedPhotos.filter(no => no !== photoNo);
		} else {
			if (selectedPhotos.length < maxPhotos) {
				$(this).addClass('selected');
				selectedPhotos.push(photoNo);
			} else {
				alert('최대 8개의 사진만 선택할 수 있습니다.');
			}
		}
		$('#selectedExistingPhotos').val(selectedPhotos.join(','));
	});

	function validateFiles(files) {
		let totalSize = 0;
		if (files.length + selectedPhotos.length > maxPhotos) {
			alert('최대 8개의 사진만 업로드할 수 있습니다.');
			return false;
		}
		for (let i = 0; i < files.length; i++) {
			const file = files[i];
			if (file.size > maxSize) {
				alert("파일 크기가 너무 큽니다. 50MB 이하의 사진 파일만 업로드 가능합니다.");
				return false;
			}
			if (!file.type.startsWith('image/')) {
				alert("이미지 파일만 업로드할 수 있습니다.");
				return false;
			}
			totalSize += file.size;
			if (totalSize > maxSize) {
				alert("전체 파일 크기가 50MB를 초과합니다. 파일 크기를 줄여주세요.");
				return false;
			}
		}
		return true;
	}

	$('#newPhotos').on('change', function() {
		const previewContainer = $('#newPhotosPreview');
		previewContainer.empty();
		const files = this.files;


		if (!validateFiles(files)) {
			this.value = '';
			return;
		}
		for (let i = 0; i < files.length; i++) {
			const file = files[i];
			const reader = new FileReader();
			reader.onload = function(e) {
				const img = $('<img>').attr('src', e.target.result).addClass('selectable-photo');
				const removeBtn = $('<button>').text('X').addClass('remove-photo');
				const newPhotoDiv = $('<div>').addClass('new-photo').append(img).append(removeBtn);
				previewContainer.append(newPhotoDiv);

				removeBtn.click(function() {
					newPhotoDiv.remove();
					if (previewContainer.children().length === 0) {
						$('#newPhotos').val('');
					}
				});
			}
			reader.readAsDataURL(file);
		}
	});

	// 폼 제출 시 파일 크기 및 형식 검증
	$('#uploadForm').on('submit', function(e) {
		const files = document.getElementById("newPhotos").files;
		if (!validateFiles(files, selectedPhotos.length)) {
			e.preventDefault();
		}
	});

	$('#communityButton').click(function(e) {
		e.preventDefault();
		if (confirm('작성 중인 내용을 저장하지 않고 나가시겠습니까?')) {
			window.location.href = contextPath + '/list.post';
		}
	});
});