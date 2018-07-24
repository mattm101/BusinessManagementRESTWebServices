package com.springproject27.springproject.vacation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {
    List<Vacation> findByUserId(Long userId);
}
