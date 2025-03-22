package com.example.demo.models.entity.constant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.example.demo.models.entity.base.AuditColumns;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbls_shift")
public class Shift extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "order")
    private int order;

    enum ShiftValues {
        AFTERNOON, MORNING
    }
}
