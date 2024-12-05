package com.mini.domain.user.service;

import com.mini.domain.user.dto.*;
import com.mini.domain.user.entity.UserEntity;
import com.mini.domain.user.repository.UserEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserEntityRepository userEntityRepository;

    public CustomOAuth2UserService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oAuth2User = {}", oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<UserEntity> existData = userEntityRepository.findByUsername(username);

        if (existData.isEmpty()) {
            UserEntity userEntity = new UserEntity(username, oAuth2Response.getName(), oAuth2Response.getEmail(), "ROLE_USER");
            userEntityRepository.save(userEntity);
            UserDTO userDTO = new UserDTO(username, oAuth2Response.getName(), "ROLE_USER");
            return new CustomOAuth2User(userDTO);
        }else{
            UserEntity user = existData.get();
            user.setEmail(oAuth2Response.getEmail());
            user.setName(oAuth2Response.getName());
            userEntityRepository.save(user);

            UserDTO userDTO = new UserDTO(user.getUsername(), oAuth2Response.getName(), user.getRole());
            return new CustomOAuth2User(userDTO);
        }

    }

}
