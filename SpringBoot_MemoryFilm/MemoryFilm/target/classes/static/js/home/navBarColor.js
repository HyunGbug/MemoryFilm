document.addEventListener('DOMContentLoaded', function () {
    var path = window.location.pathname;
    var page = path.split("/").pop();
    var pageWithoutExtension = page.split(".")[0];

    var navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(function (link) {
        link.classList.remove('color');
    });

    navLinks.forEach(function (link) {
        var linkPage = link.getAttribute('href').split("/").pop();
        var linkPageWithoutExtension = linkPage.split(".")[0];

        if (linkPageWithoutExtension.includes("HomePage") && pageWithoutExtension.includes("HomePage")) {
            if ((page === "HomePage(Login)_UI.jsp" && linkPage === "HomePage(Login)_UI.jsp") ||
                (page === "HomePage(LoginX)_UI.jsp" && linkPage === "HomePage(LoginX)_UI.jsp")) {
                link.classList.add('color');
            }
        } else if (linkPageWithoutExtension === pageWithoutExtension) {
            link.classList.add('color');
        }
    });
});
