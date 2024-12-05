package com.mini.domain.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.mini.util.TimeMaskUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 일정 ID

    private String scheduleName; // 일정 이름
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 종료 시간

    private Long userId; // 유저와의 관계

    @Transient // 데이터베이스와 관계없는 필드
    private String timeMask; // 비트마스크

    @ElementCollection
    @CollectionTable(name = "schedule_dates", joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "date")
    private List<LocalDate> dates; // 일정 날짜 목록

    public String getTimeMask() {
        if (startTime != null && endTime != null) {
            return TimeMaskUtil.convertToTimeMask(startTime, endTime);
        }
        return null;
    }
}
