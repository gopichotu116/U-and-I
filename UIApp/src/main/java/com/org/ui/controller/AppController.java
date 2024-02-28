package com.org.ui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

	/** ------------------- Welcome Page ------------------------- */

	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
}
