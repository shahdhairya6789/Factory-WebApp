package com.example.demo.models.entity.constant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

import com.example.demo.models.entity.base.AuditColumns;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "tbls_role")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AuditColumns {
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

    @Getter
    @AllArgsConstructor
    public enum RoleValues {
        ADMIN(1), EMPLOYEE(2), MERCHANT(3);
        private final int id;

        public static RoleValues fetchById(int id) {
            return Arrays.stream(RoleValues.values()).filter(template -> template.getId() == id)
                    .findFirst()
                    .orElse(null);
        }
    }
}
