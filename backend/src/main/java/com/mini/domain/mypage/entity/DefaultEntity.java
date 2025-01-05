package com.mini.domain.mypage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mini.domain.user.entity.UserEntity;
import com.mini.util.TimeMaskUtil;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    private long monday;   // 월요일 시간대 (비트마스크)
    private long tuesday;  // 화요일 시간대 (비트마스크)
    private long wednesday;  // 수요일 시간대 (비트마스크)
    private long thursday;  // 목요일 시간대 (비트마스크)
    private long friday;    // 금요일 시간대 (비트마스크)
    private long saturday;  // 토요일 시간대 (비트마스크)
    private long sunday;    // 일요일 시간대 (비트마스크)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // 비트마스크 설정
    public void setMonday(String activeTimes) { this.monday = TimeMaskUtil.generateTimeMask(activeTimes); }
    public void setTuesday(String activeTimes) {
        this.tuesday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
    public void setWednesday(String activeTimes) {
        this.wednesday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
    public void setThursday(String activeTimes) {
        this.thursday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
    public void setFriday(String activeTimes) {
        this.friday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
    public void setSaturday(String activeTimes) {
        this.saturday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
    public void setSunday(String activeTimes) {
        this.sunday = TimeMaskUtil.generateTimeMask(activeTimes);
    }
}