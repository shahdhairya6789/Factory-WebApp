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
@Entity(name = "tbls_salary_type")
@NoArgsConstructor
public class SalaryType extends AuditColumns {
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

    public SalaryType(int id) {
        this.id = id;
    }

    @Getter
    @AllArgsConstructor
    public enum SalaryTypeValues {
        ONE_MACHINE(1, "oneMachineSalary"),
        TWO_MACHINE(2, "twoMachineSalary");

        private final int id;
        private final String name;

        // Method to get ID by name
        public static SalaryTypeValues fetchByName(String name) {
            for (SalaryTypeValues enumName : SalaryTypeValues.values()) {
                if (enumName.name.equalsIgnoreCase(name)) {
                    return enumName;
                }
            }
            return null;  // Return null if not found
        }
        public static SalaryTypeValues fetchById(int id) {
            return Objects.requireNonNull(Arrays.stream(SalaryTypeValues.values()).filter(template -> template.getId() == id)
                    .findFirst()
                    .orElse(null));
        }
    }
}
