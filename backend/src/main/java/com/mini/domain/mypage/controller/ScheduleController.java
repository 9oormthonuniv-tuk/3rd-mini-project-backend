package com.mini.domain.mypage.controller;

import com.mini.domain.mypage.dto.ScheduleDTO;
import com.mini.domain.mypage.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 쿠키에서 사용자 ID 추출
    private Long extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(System.getenv("JWT"))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    // 스케쥴 등록
    public ResponseEntity<ScheduleDTO> createSchedule(
            @RequestBody ScheduleDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = extractUserIdFromToken(token);
        dto.setUserId(userId);
        ScheduleDTO createdSchedule = scheduleService.createSchedule(dto);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    // 스케쥴 수정
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id, @RequestBody ScheduleDTO dto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = extractUserIdFromToken(token);
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(id, userId, dto);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    // 스케쥴 조회
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getSchedule(
            @PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long userId = extractUserIdFromToken(token);
        ScheduleDTO schedule = scheduleService.getSchedule(id, userId);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    // 참가한 스케쥴 추가 예정
}