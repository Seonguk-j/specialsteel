package com.seah.specialsteel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    @GetMapping({"/","/result"})
    public String main(Model model) {

        return "result";
    }

    @GetMapping("/incoding")
    public String incoding(Model model) {
        return "incoding";
    }

    @GetMapping("/enroll")
    public String enrollData() {



        return null;
    }

    @GetMapping("/compare/{oriId}/{revId}")
    public String compare(@PathVariable Long oriId, @PathVariable Long revId) {



        return null;
    }
}
