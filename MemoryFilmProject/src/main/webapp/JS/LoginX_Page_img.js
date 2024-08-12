 gsap.registerPlugin(Observer); 

    let sections = document.querySelectorAll("section"),
        images = document.querySelectorAll(".bg"),
        headings = document.querySelectorAll(".section-heading"), // 바로 선택
        outerWrappers = document.querySelectorAll(".outer"), // 바로 선택
        innerWrappers = document.querySelectorAll(".inner"), // 바로 선택
        currentIndex = -1,
        wrap = gsap.utils.wrap(0, sections.length),
        animating;

    gsap.set(outerWrappers, { yPercent: 100 });
    gsap.set(innerWrappers, { yPercent: -100 });

    function gotoSection(index, direction) {
        index = wrap(index); // make sure it's valid
        animating = true;
        let fromTop = direction === -1,
            dFactor = fromTop ? -1 : 1,
            tl = gsap.timeline({
                defaults: { duration: 1.25, ease: "power1.inOut" },
                onComplete: () => animating = false
            });
        if (currentIndex >= 0) {
            // The first time this function runs, current is -1
            gsap.set(sections[currentIndex], { zIndex: 0 });
            tl.to(images[currentIndex], { yPercent: -15 * dFactor })
                .set(sections[currentIndex], { autoAlpha: 0 });
        }
        gsap.set(sections[index], { autoAlpha: 1, zIndex: 1 });
        tl.fromTo([outerWrappers[index], innerWrappers[index]], {
            yPercent: i => i ? -100 * dFactor : 100 * dFactor
        }, {
            yPercent: 0
        }, 0)
            .fromTo(images[index], { yPercent: 15 * dFactor }, { yPercent: 0 }, 0)
            .fromTo(headings[index], { // splitHeadings 대신 headings로 변경
                autoAlpha: 0,
                yPercent: 150 * dFactor
            }, {
                autoAlpha: 1,
                yPercent: 0,
                duration: 1,
                ease: "power2",
                stagger: {
                    each: 0.02,
                    from: "random"
                }
            }, 0.2);

        currentIndex = index;
    }

    let debounceTime = 500; // 밀리초 단위로 조정
let lastTime = 0;

Observer.create({
    type: "wheel,touch,pointer",
    wheelSpeed: -0.5, // 키패드 스크롤 속도 감소
    onDown: () => {
        let now = new Date().getTime();
        if (!animating && now - lastTime > debounceTime) {
            gotoSection(currentIndex - 1, -1);
            lastTime = now;
        }
    },
    onUp: () => {
        let now = new Date().getTime();
        if (!animating && now - lastTime > debounceTime) {
            gotoSection(currentIndex + 1, 1);
            lastTime = now;
        }
    },
    tolerance: 10,
    preventDefault: true
});

gotoSection(0, 1);




// Let's Start 버튼 클릭 이벤트
document.getElementById("startButton").addEventListener("click", function () {
    window.location.href = "Html/LoginForm_UI.htm"; // 예시: 로그인 페이지로 이동
});

    //nav바 색 이벤트
    var navLinks = document.querySelectorAll('nav a');

    for (var i = 0; i < navLinks.length; i++) {
        navLinks[i].addEventListener('click', function () {
            // 모든 링크에서 "active" 클래스 제거
            for (var j = 0; j < navLinks.length; j++) {
                navLinks[j].classList.remove('active');
            }
            // 클릭된 링크에 "active" 클래스 추가
            this.classList.add('active');
        });
    }
