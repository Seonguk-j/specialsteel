package com.seah.specialsteel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping({"/", "/result"})
    public String main(Model model) {

        return "result";
    }

    @GetMapping("/encoding")
    public String encoding(Model model) {
        return "encoding";
    }




}