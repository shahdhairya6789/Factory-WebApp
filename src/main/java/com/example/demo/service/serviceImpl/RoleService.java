package com.example.demo.service.serviceImpl;

import jakarta.transaction.Transactional;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.constants.ApplicationConstants;
import com.example.demo.models.CommonResponse;
import com.example.demo.models.entity.constant.Role;
import com.example.demo.repository.constant.RoleRepository;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    public CommonResponse<List<Role>> getAllRoles() {
        LOGGER.info("In RoleService.getAllRoles");
        List<Role> roles = roleRepository.findAll();
        LOGGER.info("Out RoleService.getAllRoles");
        return new CommonResponse<>(roles, ApplicationConstants.SuccessMessage.ROLE_LIST_FETCHED_SUCCESSFULLY);
    }
}
