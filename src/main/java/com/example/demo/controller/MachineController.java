package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.CreateMachineVO;
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

    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN', 'EMPLOYEE')")
    @GetMapping
    public CommonResponse<List<Machine>> getMachines() {
        LOGGER.info("In MachineController.getMachines");
        CommonResponse<List<Machine>> machineListResponse = machineService.getAllMachines();
        LOGGER.info("Out MachineController.getMachines");
        return machineListResponse;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN','EMPLOYEE')")
    @GetMapping("/{id}")
    public CommonResponse<Machine> getMachine(@PathVariable Integer id) {
        LOGGER.info("In MachineController.getMachine");
        CommonResponse<Machine> machine = machineService.getMachineById(id);
        LOGGER.info("Out MachineController.getMachine");
        return machine;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN')")
    @PostMapping
    public CommonResponse<Machine> createMachine(@RequestBody CreateMachineVO createMachineVO) {
        LOGGER.info("In MachineController.createMachine");
        CommonResponse<Machine> machine = machineService.saveMachineDetails(createMachineVO);
        LOGGER.info("Out MachineController.createMachine");
        return machine;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN')")
    @PutMapping("/{id}")
    public CommonResponse<Machine> updateMachine(@PathVariable Integer id,
                                                 @RequestBody CreateMachineVO createMachineVO) {
        LOGGER.info("In MachineController.updateMachine");
        CommonResponse<Machine> machine = machineService.updateMachineDetails(id, createMachineVO);
        LOGGER.info("Out MachineController.updateMachine");
        return machine;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN')")
    @DeleteMapping("/{id}")
    public CommonResponse<String> deleteMachine(@PathVariable Integer id) {
        LOGGER.info("In MachineController.deleteMachine");
        CommonResponse<String> response = machineService.deleteMachineDetails(id);
        LOGGER.info("Out MachineController.deleteMachine");
        return response;
    }
}
