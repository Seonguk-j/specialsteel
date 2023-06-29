package com.seah.specialsteel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(Model model) {

        return "result";
    }

    @GetMapping("/result")
    public String result(Model model) {
        return "result";
    }


    @GetMapping("/incoding")
    public String incoding(Model model) {
        return "incoding";
    }

}
