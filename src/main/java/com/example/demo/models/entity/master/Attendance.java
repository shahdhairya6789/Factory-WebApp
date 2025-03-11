package com.example.demo.models.entity.master;

import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

import com.example.demo.models.entity.base.AuditColumns;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.constant.Shift;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tblm_attendance")
public class Attendance extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private Timestamp attendanceDate;
    private String production;
    private String dhaga;
    private int attendanceUserImageSize;
    private String attendanceUserImageName;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "machine_id", referencedColumnName = "id")
    private Machine machine;
    @ManyToOne
    @JoinColumn(name = "salary_type_id", referencedColumnName = "id")
    private SalaryType salaryType;
    @ManyToOne
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;


}
