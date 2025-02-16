package com.example.demo.models.entity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.example.demo.models.entity.base.AuditColumns;

@Entity(name = "tblm_machine")
public class Machine extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String heads;
    private String area;
    private boolean active;
}
