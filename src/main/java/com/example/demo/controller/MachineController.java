package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.entity.master.Machine;
import com.example.demo.service.MachineService;

/**
 * The controller for the machines.
 * The endpoints for CRUD operations on machines.
 */
@RestController
@RequestMapping("/v1/machines")
public class MachineController {
    private final MachineService machineService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MachineController.class);

    @Autowired
    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public List<Machine> getMachines() {
        LOGGER.info("In MachineController.getMachines");
        List<Machine> machineList = machineService.getAllMachines();
        LOGGER.info("Out MachineController.getMachines");
        return machineList;
    }

    @GetMapping("/{id}")
    public Machine getMachine(@PathVariable Integer id) {
        LOGGER.info("In MachineController.getMachine");
        Machine machine = machineService.getMachineById(id);
        LOGGER.info("Out MachineController.getMachine");
        return machine;
    }

    @PostMapping
    public Machine createMachine(@RequestBody Machine machine) {
        LOGGER.info("In MachineController.createMachine");
        machineService.saveMachineDetails(machine);
        LOGGER.info("Out MachineController.createMachine");
        return machine;
    }

    @PutMapping
    public Machine updateMachine(@RequestBody Machine machine) {
        LOGGER.info("In MachineController.updateMachine");
        machineService.updateMachineDetails(1, machine);
        LOGGER.info("Out MachineController.updateMachine");
        return machine;
    }

    @DeleteMapping("/{id}")
    public void deleteMachine(@PathVariable Integer id) {
        LOGGER.info("In MachineController.deleteMachine");
        machineService.deleteMachineDetails(id);
        LOGGER.info("Out MachineController.deleteMachine");
    }
}
