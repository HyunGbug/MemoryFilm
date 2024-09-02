// src/main/resources/static/js/gradientBG.js

class GradientBG {
    constructor(width = 1920, height = 1080, target = document.body) {
        this.width = width;
        this.height = height;
        this.target = target;

        this.svgns = "http://www.w3.org/2000/svg";
        this.SVGElement = this._createSVGElement();

        this.id = Math.random();
    }

    generate() {
		if (typeof tinycolor === 'undefined') {
		         console.error('tinycolor is not defined');
		         return;
		     }
			 
        this.SVGElement.innerHTML = "";
        this.defs = this._createDefs();
        this.backgroundGradient = this._createBackgroundGradient();
        this.blurFilter = this._createBlurFilter();

        // tinycolor 전역 객체 사용
        let baseColor = tinycolor(
            `hsl(${~~this._random(0, 360)}, ${this._random(75, 100)}, ${this._random(80, 92)}%)`
        );

        let combinations = baseColor.splitcomplement();
        let secondaryColor = combinations[~~this._random(1, combinations.length)];

        let secondaryCombinations = tinycolor(secondaryColor).splitcomplement();

        const stopOffset1 = this._random(0, 25);
        const stopOffset2 = 100 - this._random(0, 25);

        this.backgroundGradient.stop1.setAttribute(
            "stop-color",
            baseColor.toHslString()
        );
        this.backgroundGradient.stop2.setAttribute(
            "stop-color",
            secondaryColor.toHslString()
        );

        this.backgroundGradient.stop1.setAttribute("offset", `${stopOffset1}%`);
        this.backgroundGradient.stop2.setAttribute("offset", `${stopOffset2}%`);

        const maxBlobSize = Math.min(this.width, this.height);

        this._blob({
            x: `${this._random(0, this.width / 4)}`,
            y: `${this._random(0, this.height / 4)}`,
            r: `${this._random(maxBlobSize / 4, maxBlobSize / 2)}`,
            fill: combinations[~~this._random(0, combinations.length)]
        });

        this._blob({
            x: `${this._random(this.width - this.width / 4, this.width)}`,
            y: `${this._random(0, this.height / 4)}`,
            r: `${this._random(maxBlobSize / 4, maxBlobSize / 2)}`,
            fill: combinations[~~this._random(0, combinations.length)]
        });

        this._blob({
            x: `${this._random(0, this.width / 4)}`,
            y: `${this._random(this.height - this.height / 4, this.height)}`,
            r: `${this._random(maxBlobSize / 4, maxBlobSize / 2)}`,
            fill: secondaryCombinations[~~this._random(0, secondaryCombinations.length)]
        });

        this._blob({
            x: `${this._random(this.width - this.width / 4, this.width)}`,
            y: `${this._random(this.height - this.height / 4, this.height)}`,
            r: `${this._random(maxBlobSize / 4, maxBlobSize / 2)}`,
            fill: secondaryCombinations[~~this._random(0, secondaryCombinations.length)]
        });
    }

    _createSVGElement() {
        const el = document.createElementNS(this.svgns, "svg");

        el.setAttribute("viewBox", `0 0 ${this.width} ${this.height}`);
        el.setAttribute("preserveAspectRatio", "xMidYMid slice");

        this.target.appendChild(el);

        return el;
    }

    _createDefs() {
        const el = document.createElementNS(this.svgns, "defs");

        this.SVGElement.appendChild(el);

        return el;
    }

    _createBackgroundGradient() {
        const el = document.createElementNS(this.svgns, "linearGradient");

        el.id = "bgGradient" + this.id;
        el.setAttribute("gradientTransform", "rotate(90)");

        const stop1 = document.createElementNS(this.svgns, "stop");
        stop1.setAttribute("offset", `${~~this._random(25, 25)}%`);

        const stop2 = document.createElementNS(this.svgns, "stop");
        stop2.setAttribute("offset", `${~~this._random(80, 100)}%`);

        el.appendChild(stop1);
        el.appendChild(stop2);

        this.defs.appendChild(el);

        const rect = document.createElementNS(this.svgns, "rect");

        rect.setAttribute("x", "0");
        rect.setAttribute("y", "0");
        rect.setAttribute("width", "100%");
        rect.setAttribute("height", "100%");
        rect.setAttribute("fill", `url(#bgGradient${this.id})`);

        this.SVGElement.appendChild(rect);

        return {
            rect: rect,
            stop1: stop1,
            stop2: stop2
        };
    }

    _createBlurFilter() {
        const el = document.createElementNS(this.svgns, "filter");

        el.id = "blur" + this.id;
        el.setAttribute("x", "-100%");
        el.setAttribute("y", "-100%");
        el.setAttribute("width", "300%");
        el.setAttribute("height", "300%");

        const gaussianBlur = document.createElementNS(this.svgns, "feGaussianBlur");

        gaussianBlur.setAttribute("in", "SourceGraphic");
        gaussianBlur.setAttribute("stdDeviation", this._random(50, 100));

        el.appendChild(gaussianBlur);

        this.defs.appendChild(el);

        return el;
    }

    _blob({ x, y, r, fill, filter = `url(#blur${this.id})` }) {
        const circle = document.createElementNS(this.svgns, "circle");

        circle.setAttribute("cx", x);
        circle.setAttribute("cy", y);
        circle.setAttribute("r", r);

        circle.setAttribute("fill", fill);
        circle.setAttribute("filter", filter);

        this.SVGElement.appendChild(circle);

        return circle;
    }

    _random(min, max) {
        return Math.random() * (max - min) + min;
    }
}

export default GradientBG;
