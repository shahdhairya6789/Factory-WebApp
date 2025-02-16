package com.example.demo.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.master.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
}
