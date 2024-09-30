package com.hunmin.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/api/members/admin")
    public String adminP() {
        return "admin Controller";
    }
}
