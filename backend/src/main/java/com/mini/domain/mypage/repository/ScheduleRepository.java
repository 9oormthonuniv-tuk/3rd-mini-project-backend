package com.mini.domain.mypage.repository;

import com.mini.domain.mypage.entity.DefaultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<DefaultEntity, Long> {
    Optional<DefaultEntity> findByIdAndUserId(Long id, Long userId);
}