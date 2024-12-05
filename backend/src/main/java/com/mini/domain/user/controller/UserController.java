package com.mini.domain.user.controller;

import com.mini.domain.user.dto.CustomOAuth2User;
import com.mini.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/a")
    public String a(@AuthenticationPrincipal CustomOAuth2User userDetails) {
        return userService.a(userDetails.getUsername());
    }

}
