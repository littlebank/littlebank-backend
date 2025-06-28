package com.littlebank.finance.global.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deeplink")
public class LinkViewController {

    @GetMapping("/invite")
    public String index() {
        return "deeplink/invite-deeplink-page";
    }
}
