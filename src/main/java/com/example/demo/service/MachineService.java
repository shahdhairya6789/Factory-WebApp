package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.entity.master.Machine;
import com.example.demo.repository.master.MachineRepository;

@Service
@Transactional
public class MachineService {

    private final MachineRepository machineRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MachineService.class);

    @Autowired
    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public List<Machine> getAllMachines() {
        LOGGER.info("In MachineService.getAllMachines");
        List<Machine> machines = machineRepository.findAll();
        LOGGER.info("Out MachineService.getAllMachines");
        return machines;
    }

    public Machine getMachineById(int id) {
        LOGGER.info("In MachineService.getMachineById");
        Machine machine = machineRepository.findById(id);
        LOGGER.info("Out MachineService.getMachineById");
        return machine;
    }

    public void saveMachineDetails(Machine machine) {
        LOGGER.info("In MachineService.saveMachineDetails");
        machineRepository.save(machine);
        LOGGER.info("Out MachineService.saveMachineDetails");
    }

    public void updateMachineDetails(int id, Machine machine) {
        LOGGER.info("In MachineService.updateMachineDetails");
        machine = new Machine(machineRepository.getReferenceById((long) id));
        saveMachineDetails(machine);
        LOGGER.info("Out MachineService.updateMachineDetails");
    }

    public void deleteMachineDetails(int id) {
        LOGGER.info("In MachineService.deleteMachineDetails");
        Machine machine = machineRepository.getReferenceById((long) id);
        machine.setActive(Boolean.FALSE);
        saveMachineDetails(machine);
        LOGGER.info("Out MachineService.deleteMachineDetails");
    }
}
