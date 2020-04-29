package com.yanyun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class ThymeleafController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}
