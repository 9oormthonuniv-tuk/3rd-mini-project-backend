package com.mini.domain.user.service;

import com.mini.domain.user.UserRepository;
import com.mini.domain.user.dto.*;
import com.mini.domain.user.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {
            UserEntity userEntity = new UserEntity(username, oAuth2Response.getName(), oAuth2Response.getEmail(), "ROLE_USER");
            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO(username, oAuth2Response.getName(), "ROLE_USER");
            return new CustomOAuth2User(userDTO);
        }else{
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            userRepository.save(existData);

            UserDTO userDTO = new UserDTO(existData.getUsername(), oAuth2Response.getName(), existData.getRole());
            return new CustomOAuth2User(userDTO);
        }

    }

}
