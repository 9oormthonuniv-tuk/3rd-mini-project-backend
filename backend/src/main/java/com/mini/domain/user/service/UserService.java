package com.mini.domain.user.service;

import com.mini.domain.user.entity.UserEntity;
import com.mini.domain.user.repository.UserEntityRepository;
import com.mini.global.aop.LoggingAspect;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;

    public String a(String username) {
        return userEntityRepository.findByUsername(username).get().toString();
    }
}
