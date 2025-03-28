package ru.testPr.test.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	@GetMapping("/")
    public String home() {
        return "index";
    }
	
	@GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
