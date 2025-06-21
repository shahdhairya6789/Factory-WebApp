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
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import com.example.demo.models.dto.AttendanceVO;
import com.example.demo.models.entity.base.AuditColumns;
import com.example.demo.models.entity.constant.SalaryType;
import com.example.demo.models.entity.constant.Shift;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tblm_attendance")
@NoArgsConstructor
public class Attendance extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp attendanceDate;
    private int production;
    private int dhaga;
    private int frames;
    private int attendanceUserImageSize;
    private String attendanceUserImageName;
    private String attendanceUserImagePath;
    private boolean isActive;
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
    private int createdBy;
    private int modifiedBy;

    public Attendance(AttendanceVO attendanceVO,
                      User user,
                      Machine machine,
                      Shift shift,
                      SalaryType salaryType,
                      String fileName,
                      long fileSize,
                      String filePath, int createdBy, int modifiedBy) {
        this.attendanceDate = new Timestamp(attendanceVO.getAttendanceDate()* 1000L);
        this.production = attendanceVO.getProduction();
        this.dhaga = attendanceVO.getDhaga();
        this.frames = attendanceVO.getFrames();
        this.user = user;
        this.machine = machine;
        this.shift = shift;
        this.salaryType = salaryType;
        this.isActive = true;
        this.attendanceUserImageName = fileName;
        this.attendanceUserImageSize = (int) fileSize;
        this.attendanceUserImagePath = filePath;
        this.createdBy=createdBy;
        this.modifiedBy=modifiedBy;
    }
}
