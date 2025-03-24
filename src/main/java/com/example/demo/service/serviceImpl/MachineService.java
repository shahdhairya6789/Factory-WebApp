package com.example.demo.service.serviceImpl;

import java.util.List;
import java.util.Objects;

import com.example.demo.models.entity.master.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.CommonResponse;
import com.example.demo.models.dto.CreateMachineVO;
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

    public CommonResponse<List<Machine>> getAllMachines() {
        LOGGER.info("In MachineService.getAllMachines");
        List<Machine> machines = machineRepository.findByIsActiveTrue();
        LOGGER.info("Out MachineService.getAllMachines");
        return new CommonResponse<>(machines, ApplicationConstants.SuccessMessage.MACHINE_LIST_FETCHED_SUCCESSFULLY);
    }

    public CommonResponse<List<Machine>> getMachineByUserId(Integer userId) {
        LOGGER.info("In MachineService.getMachineByUserId");
        List<Machine> machines = machineRepository.findByUserId(userId);
        LOGGER.info("Out MachineService.getMachineByUserId");
        return new CommonResponse<>(machines, ApplicationConstants.SuccessMessage.MACHINE_LIST_FETCHED_SUCCESSFULLY);
    }

    public CommonResponse<Machine> getMachineById(int id) {
        LOGGER.info("In MachineService.getMachineById");
        Machine machine = machineRepository.findById(id).orElse(null);
        LOGGER.info("Out MachineService.getMachineById");
        return new CommonResponse<>(machine, ApplicationConstants.SuccessMessage.MACHINE_DETAILS_SUCCESSFULLY);
    }

    public CommonResponse<Machine> saveMachineDetails(CreateMachineVO createMachineVO) {
        LOGGER.info("In MachineService.saveMachineDetails");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
        Machine machine = new Machine();
        machine.setName(createMachineVO.getName());
        machine.setArea(createMachineVO.getArea());
        machine.setHeads(createMachineVO.getHeads());
        machine.setActive(createMachineVO.isActive());
        machine.setUserId(currentUserId);
        machine.setModifiedBy(currentUserId);
        machine.setCreatedBy(currentUserId);
        machine = machineRepository.save(machine);
        LOGGER.info("Out MachineService.saveMachineDetails");
        return new CommonResponse<>(machine, ApplicationConstants.SuccessMessage.MACHINE_SAVED_SUCCESSFULLY);
    }

    public CommonResponse<Machine> updateMachineDetails(int id, CreateMachineVO createMachineVO) {
        LOGGER.info("In MachineService.updateMachineDetails: {}",id);
        Machine machine = machineRepository.findById(id).orElse(null);
            if (Objects.isNull(machine) || !machine.isActive()) {
            throw new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_MACHINE_ID_MESSAGE);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
        machine.setId(machine.getId());
        machine.setName(Objects.isNull(createMachineVO.getName()) ? machine.getName() : createMachineVO.getName());
        machine.setArea(Objects.isNull(createMachineVO.getArea()) ? machine.getArea() : createMachineVO.getArea());
        machine.setHeads(Objects.isNull(createMachineVO.getHeads()) ? machine.getHeads() : createMachineVO.getHeads());
        machine.setModifiedBy(currentUserId);
        machine = machineRepository.save(machine);
        LOGGER.info("Out MachineService.updateMachineDetails id: {}", id);
        return new CommonResponse<>(machine, ApplicationConstants.SuccessMessage.MACHINE_UPDATED_SUCCESSFULLY);
    }

    public CommonResponse<String> deleteMachineDetails(int id) {
        LOGGER.info("In MachineService.deleteMachineDetails");
        Machine machine = machineRepository.findById(id).orElse(null);
        if (Objects.isNull(machine) || !machine.isActive()) {
            throw new IllegalArgumentException(ApplicationConstants.ValidationMessage.INVALID_MACHINE_ID_MESSAGE);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer currentUserId = ((CustomUserDetails)authentication.getPrincipal()).getUserId();
        machine.setActive(Boolean.FALSE);
        machine.setModifiedBy(currentUserId);
        machineRepository.save(machine);
        LOGGER.info("Out MachineService.deleteMachineDetails");
        return new CommonResponse<>(ApplicationConstants.SuccessMessage.MACHINE_DELETED_SUCCESSFULLY);
    }
}
