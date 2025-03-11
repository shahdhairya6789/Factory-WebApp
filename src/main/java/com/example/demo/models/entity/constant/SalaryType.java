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
@Entity(name = "tbls_salary_type")
public class SalaryType extends StaticTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    enum SalaryTypeValues {
        ONE_MACHINE, TWO_MACHINE
    }
}
