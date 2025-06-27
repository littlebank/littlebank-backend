package com.littlebank.finance.global.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view")
@Controller
public class ViewController {

    @GetMapping("/public")
    public String index() {
        return "index";
    }

    @GetMapping("/public/login")
    public String login() {
        return "login";
    }

    @GetMapping("/public/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/main")
    public String main() {
        return "main";
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage";
    }
}