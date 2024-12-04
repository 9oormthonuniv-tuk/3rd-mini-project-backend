package com.mini.domain.user;


import com.mini.domain.user.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    Boolean existsByRefresh(String refresh);

    void deleteByRefresh(String refresh);

}
