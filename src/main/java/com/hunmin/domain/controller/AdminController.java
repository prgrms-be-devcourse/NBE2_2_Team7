package com.hunmin.domain.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// ADMIN 컨트롤러 구현
@RestController
public class AdminController {

    @GetMapping("/api/members/admin")
    public String adminP() {
        return "ADMIN USER CONTROLLER";
    }
}
