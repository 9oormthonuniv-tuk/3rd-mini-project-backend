package com.mini.global.jwt;

import com.mini.domain.user.dto.CustomOAuth2User;
import com.mini.domain.user.dto.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
        String accessToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("cookie.getName = {}", cookie.getName());
                if (cookie.getName().equals("access")) {
                    accessToken = cookie.getValue();
                }
            }
        }

        // accessToken이 없으면 다음 필터로 이동
        if (accessToken == null || accessToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 엑세스 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            log.info("token is expired");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        } catch (IllegalArgumentException e) {
            log.info("token is invalid");
            filterChain.doFilter(request, response);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            log.info("token is not access");

            filterChain.doFilter(request, response);
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = new UserDTO(username, role);

        //UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
