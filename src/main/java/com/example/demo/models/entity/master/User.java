package com.example.demo.models.entity.master;

import com.example.demo.models.entity.constant.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.example.demo.models.entity.base.AuditColumns;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore
    private String otp;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private User manager;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tblt_user_role_mapping",
            joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") }
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "created_by_user_id")
    @JsonIgnore
    private Integer createdBy;
    @Column(name = "modified_by_user_id")
    @JsonIgnore
    private Integer modifiedBy;
}
