package com.film.www.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping("/loginMain")
	public String getLoginMainPage() {
		return "login/Login"; // "login/Login.html" 파일을 의미
	}
	
	@GetMapping("/allUserHome")
	public String getAllUserHomePage() {
		return "home/Home(LoginX)";
	}
	
	@GetMapping("/signUp")
		public String getSignUpPage() {
			return "login/SignUp";
	}
	
	@GetMapping("/about")
	public String getAboutPage() {
		return "about/About";
	}
}
