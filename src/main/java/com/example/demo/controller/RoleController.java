package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.CommonResponse;
import com.example.demo.models.entity.constant.Role;
import com.example.demo.service.serviceImpl.RoleService;

@RestController
@RequestMapping("/v1/roles")
public class RoleController {

    private RoleService roleService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAnyAuthority('MERCHANT', 'ADMIN', 'EMPLOYEE')")
    @GetMapping
    public CommonResponse<List<Role>> getRoles() {
        LOGGER.info("In RoleService.getRoles");
        CommonResponse<List<Role>> response = roleService.getAllRoles();
        LOGGER.info("Out RoleService.getRoles");
        return response;
    }
}
