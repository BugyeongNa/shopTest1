package com.springstudy.shop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    //@PreAuthorize("hasRole('ADMIN')")
    //@PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping(value="/")
    public String main(){
        return "index";
    }
}
