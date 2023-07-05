package com.seah.specialsteel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/","/result"})
    public String main(Model model) {

        return "result";
    }


    @GetMapping("/encoding")
    public String incoding(Model model) {
        return "encoding";
    }

}
