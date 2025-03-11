package com.example.demo.models.entity.audit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.example.demo.models.entity.base.AuditColumns;
import com.example.demo.models.entity.master.User;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbla_user")
public class UserAudit extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private boolean active;
    private String email;
    private String mobileNumber;
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private User manager;
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id")
    private User createdByUser;
    @ManyToOne
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "id")
    private User modifiedByUser;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
