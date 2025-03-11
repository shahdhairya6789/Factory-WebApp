package com.example.demo.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.master.Machine;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {
    Machine findById(int machineId);
}
