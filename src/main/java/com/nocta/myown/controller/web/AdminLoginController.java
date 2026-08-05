package com.nocta.myown.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminLoginController {

    @GetMapping("/admin-login")
    public String mostrarLogin() {
        return "admin/login";
    }
}