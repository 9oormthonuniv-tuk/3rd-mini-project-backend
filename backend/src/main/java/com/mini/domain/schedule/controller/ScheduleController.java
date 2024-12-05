package com.mini.domain.schedule.controller;

import com.mini.domain.schedule.dto.ScheduleRequestDto;
import com.mini.domain.schedule.entity.Schedule;
import com.mini.domain.schedule.repository.ScheduleRepository;
import com.mini.domain.user.dto.CustomOAuth2User;
import com.mini.domain.user.entity.UserEntity;
import com.mini.domain.user.repository.UserEntityRepository;
import com.mini.util.TimeMaskUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;
    private final UserEntityRepository userEntityRepository;

    // 일정 생성
    @PostMapping
    public Schedule createSchedule(@AuthenticationPrincipal CustomOAuth2User userDetails,
                                   @RequestBody ScheduleRequestDto requestDto) {
        Optional<UserEntity> user = userEntityRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent()) {
            // 문자열 비트마스크를 long으로 변환
            long timeMask = TimeMaskUtil.convertStringToLong(requestDto.getTimeMask());

            // 비트마스크로부터 시작/종료 시간 계산
            LocalDateTime[] times = TimeMaskUtil.getStartAndEndTime(timeMask);
            LocalDateTime startTime = times[0];
            LocalDateTime endTime = times[1];

            // 일정 생성 및 저장
            Schedule schedule = new Schedule();
            schedule.setScheduleName(requestDto.getScheduleName());
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setUserId(user.get().getId());
            return scheduleRepository.save(schedule);
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }

    // 일정 수정
    @PutMapping("/{id}")
    public Schedule updateSchedule(@AuthenticationPrincipal CustomOAuth2User userDetails,
                                   @PathVariable Long id,
                                   @RequestBody ScheduleRequestDto requestDto) {
        Optional<UserEntity> user = userEntityRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent()) {
            Schedule schedule = scheduleRepository.findByIdAndUserId(id, user.get().getId())
                    .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없거나 권한이 없습니다."));

            // 문자열 비트마스크를 long으로 변환
            long timeMask = TimeMaskUtil.convertStringToLong(requestDto.getTimeMask());

            // 비트마스크로부터 시작/종료 시간 계산
            LocalDateTime[] times = TimeMaskUtil.getStartAndEndTime(timeMask);
            LocalDateTime startTime = times[0];
            LocalDateTime endTime = times[1];

            // 일정 수정 및 저장
            schedule.setScheduleName(requestDto.getScheduleName());
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            return scheduleRepository.save(schedule);
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public String deleteSchedule(@AuthenticationPrincipal CustomOAuth2User userDetails,
                                 @PathVariable Long id) {
        Optional<UserEntity> user = userEntityRepository.findByUsername(userDetails.getUsername());
        if (user.isPresent()) {
            Schedule schedule = scheduleRepository.findByIdAndUserId(id, user.get().getId())
                    .orElseThrow(() -> new RuntimeException("일정을 찾을 수 없거나 권한이 없습니다."));
            scheduleRepository.delete(schedule);
            return "일정이 성공적으로 삭제되었습니다.";
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }
}
