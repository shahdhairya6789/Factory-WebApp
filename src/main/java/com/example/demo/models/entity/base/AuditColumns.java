package com.example.demo.models.entity.base;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;

@Data
public class AuditColumns {

    @NotNull
    @Column(name = "created_at",
            columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @NotNull
    @Column(
            name = "modified_at",
            columnDefinition = "timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp modifiedAt = new Timestamp(System.currentTimeMillis());
}
