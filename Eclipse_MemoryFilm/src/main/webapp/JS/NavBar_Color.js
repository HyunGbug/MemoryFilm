document.addEventListener('DOMContentLoaded', function () {
    // 현재 URL 경로를 가져옴
    var path = window.location.pathname;
    var page = path.split("/").pop(); // URL에서 파일명만 추출
    var pageWithoutExtension = page.split(".")[0]; // 파일명에서 확장자를 제거

    // 모든 네비게이션 링크 요소를 가져옴
    var navLinks = document.querySelectorAll('.nav-link');

    // 모든 링크에서 'active' 클래스 제거
    navLinks.forEach(function (link) {
        link.classList.remove('color');
    });

    // 현재 페이지에 해당하는 링크에 'active' 클래스를 추가
    navLinks.forEach(function (link) {
        var linkPage = link.getAttribute('href').split("/").pop(); // 링크의 파일명 추출
        var linkPageWithoutExtension = linkPage.split(".")[0]; // 링크의 파일명에서 확장자를 제거

        // Home 링크의 경우 특별히 처리
        if (linkPageWithoutExtension.includes("HomePage") && pageWithoutExtension.includes("HomePage")) {
            // 두 HomePage 파일 중 하나인 경우에만 'active' 클래스 추가
            if ((page === "HomePage(Login)_UI.jsp" && linkPage === "HomePage(Login)_UI.jsp") ||
                (page === "HomePage(LoginX)_UI.jsp" && linkPage === "HomePage(LoginX)_UI.jsp")) {
                link.classList.add('color');
            }
        } else if (linkPageWithoutExtension === pageWithoutExtension) {
            link.classList.add('color');
        }
    });
});
