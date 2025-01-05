package com.mini.domain.schedule.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScheduleRequestDto {
    private String scheduleName;
    private String timeMask;
    private List<LocalDate> dates;
}

