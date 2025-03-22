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
import java.util.Objects;

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
        ADMIN(1, "Admin"),
        MERCHANT(2, "Merchant"),
        EMPLOYEE(3, "Employee");

        private final int id;
        private final String name;

        // Method to get ID by name
        public static Integer fetchIdByName(String roleName) {
            for (RoleValues role : RoleValues.values()) {
                if (role.name.equalsIgnoreCase(roleName)) {
                    return role.id;
                }
            }
            return null;  // Return null if not found
        }
        public static String fetchById(int id) {
            return Objects.requireNonNull(Arrays.stream(RoleValues.values()).filter(template -> template.getId() == id)
                    .findFirst()
                    .orElse(null)).name;
        }
    }
}
