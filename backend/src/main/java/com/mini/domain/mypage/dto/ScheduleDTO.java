package com.mini.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDTO {
    private Long id;
    private String title;
    private String description;
    private long mondayTimes;    // 월요일 시간대
    private long tuesdayTimes;   // 화요일 시간대
    private long wednesdayTimes; // 수요일 시간대
    private long thursdayTimes;  // 목요일 시간대
    private long fridayTimes;    // 금요일 시간대
    private long saturdayTimes;  // 토요일 시간대
    private long sundayTimes;    // 일요일 시간대
    private Long userId;
}