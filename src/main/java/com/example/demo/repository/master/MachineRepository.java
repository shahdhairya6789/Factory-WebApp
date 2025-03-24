package com.example.demo.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.master.Machine;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    Optional<Machine> findById(int machineId);
    List<Machine> findByIsActiveTrue();
    List<Machine> findByUserId(Integer userId);
}
