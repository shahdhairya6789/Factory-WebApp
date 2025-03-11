package com.example.demo.models.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StaticTable extends AuditColumns {
    private String name;
    private String displayName;
    private boolean active;
    private int order;
}
