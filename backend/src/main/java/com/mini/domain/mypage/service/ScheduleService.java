package com.mini.domain.mypage.service;

import com.mini.domain.mypage.dto.ScheduleDTO;
import com.mini.domain.mypage.entity.DefaultEntity;
import com.mini.domain.mypage.repository.ScheduleRepository;
import com.mini.domain.user.entity.UserEntity;
import com.mini.domain.user.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserEntityRepository userEntityRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, UserEntityRepository userEntityRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userEntityRepository = userEntityRepository;
    }

    // 스케쥴 등록
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO dto) {
        UserEntity user = userEntityRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        DefaultEntity schedule = new DefaultEntity();
        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());

        // 요일별 시간대 설정 (비트마스크 변환)
        schedule.setMonday(String.valueOf(dto.getMondayTimes()));
        schedule.setTuesday(String.valueOf(dto.getTuesdayTimes()));
        schedule.setWednesday(String.valueOf(dto.getWednesdayTimes()));
        schedule.setThursday(String.valueOf(dto.getThursdayTimes()));
        schedule.setFriday(String.valueOf(dto.getFridayTimes()));
        schedule.setSaturday(String.valueOf(dto.getSaturdayTimes()));
        schedule.setSunday(String.valueOf(dto.getSundayTimes()));

        schedule.setUser(user);

        DefaultEntity savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleDTO(
                savedSchedule.getId(),
                savedSchedule.getTitle(),
                savedSchedule.getDescription(),
                savedSchedule.getMonday(),
                savedSchedule.getTuesday(),
                savedSchedule.getWednesday(),
                savedSchedule.getThursday(),
                savedSchedule.getFriday(),
                savedSchedule.getSaturday(),
                savedSchedule.getSunday(),
                user.getId()
        );
    }

    // 스케쥴 수정
    @Transactional
    public ScheduleDTO updateSchedule(Long id, Long userId, ScheduleDTO dto) {
        DefaultEntity schedule = scheduleRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());

        // 요일별 시간대 수정 (비트마스크 변환)
        schedule.setMonday(String.valueOf(dto.getMondayTimes()));
        schedule.setTuesday(String.valueOf(dto.getTuesdayTimes()));
        schedule.setWednesday(String.valueOf(dto.getWednesdayTimes()));
        schedule.setThursday(String.valueOf(dto.getThursdayTimes()));
        schedule.setFriday(String.valueOf(dto.getFridayTimes()));
        schedule.setSaturday(String.valueOf(dto.getSaturdayTimes()));
        schedule.setSunday(String.valueOf(dto.getSundayTimes()));

        DefaultEntity updatedSchedule = scheduleRepository.save(schedule);

        return new ScheduleDTO(
                updatedSchedule.getId(),
                updatedSchedule.getTitle(),
                updatedSchedule.getDescription(),
                updatedSchedule.getMonday(),
                updatedSchedule.getTuesday(),
                updatedSchedule.getWednesday(),
                updatedSchedule.getThursday(),
                updatedSchedule.getFriday(),
                updatedSchedule.getSaturday(),
                updatedSchedule.getSunday(),
                userId
        );
    }

    // 스케쥴 조회
    public ScheduleDTO getSchedule(Long id, Long userId) {
        DefaultEntity schedule = scheduleRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        return new ScheduleDTO(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getDescription(),
                schedule.getMonday(),
                schedule.getTuesday(),
                schedule.getWednesday(),
                schedule.getThursday(),
                schedule.getFriday(),
                schedule.getSaturday(),
                schedule.getSunday(),
                userId
        );
    }
}