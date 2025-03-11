package com.example.demo.models.entity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.example.demo.models.entity.base.AuditColumns;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tblm_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends AuditColumns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @JsonIgnore
    private String password;
    private boolean isActive;
    private String email;
    private String mobileNumber;
    private String loginToken;
    private String otp;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private User manager;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "created_by_user_id", referencedColumnName = "id")
    private User createdByUser;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "modified_by_user_id", referencedColumnName = "id")
    private User modifiedByUser;
}
