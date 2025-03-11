package com.example.demo.models.entity.constant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.example.demo.models.entity.base.StaticTable;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbls_role")
public class Role extends StaticTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    enum RoleValues {
        ADMIN, EMPLOYEE, MERCHANT
    }
}
