package com.mini.domain;


import com.mini.domain.user.dto.CustomOAuth2User;
import com.mini.domain.user.entity.UserEntity;
import com.mini.domain.user.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/main")
@Slf4j
@AllArgsConstructor
public class MainController {

    private final UserEntityRepository userEntityRepository;

    @GetMapping("/a")
    public String a(@AuthenticationPrincipal CustomOAuth2User userDetails) {
        Optional<UserEntity> User = userEntityRepository.findByUsername(userDetails.getUsername());
        return "main.a";
    }
}
