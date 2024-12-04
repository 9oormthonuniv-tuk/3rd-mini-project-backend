package com.mini.domain;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
@Slf4j
public class MainController {

    @GetMapping("/a")
    public String a() {
        log.info("a");
        return "main.a";
    }
}
