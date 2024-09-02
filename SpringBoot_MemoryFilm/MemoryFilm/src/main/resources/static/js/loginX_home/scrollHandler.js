gsap.registerPlugin(Observer);

let sections = document.querySelectorAll("section"),
    images = document.querySelectorAll(".bg"),
    headings = document.querySelectorAll(".section-heading"),
    outerWrappers = document.querySelectorAll(".outer"),
    innerWrappers = document.querySelectorAll(".inner"),
    currentIndex = -1,
    wrap = gsap.utils.wrap(0, sections.length),
    animating;

gsap.set(outerWrappers, { yPercent: 100 });
gsap.set(innerWrappers, { yPercent: -100 });

function gotoSection(index, direction) {
    index = wrap(index); 
    animating = true;
    let fromTop = direction === -1,
        dFactor = fromTop ? -1 : 1,
        tl = gsap.timeline({
            defaults: { duration: 1.25, ease: "power1.inOut" },
            onComplete: () => animating = false
        });
    if (currentIndex >= 0) {
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
        .fromTo(headings[index], {
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

let debounceTime = 500; 
let lastTime = 0;

Observer.create({
    type: "wheel,touch,pointer",
    wheelSpeed: -0.5,
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

document.getElementById("startButton").addEventListener("click", function () {
           window.location.href = "/loginMain"; 
       });
	   
gotoSection(0, 1);
