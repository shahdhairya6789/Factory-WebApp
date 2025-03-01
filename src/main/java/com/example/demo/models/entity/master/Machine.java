package com.example.demo.models.entity.master;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

import com.example.demo.models.entity.base.AuditColumns;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "tblm_machine")
public class Machine extends AuditColumns implements Serializable {

    @JsonProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("heads")
    private String heads;

    @JsonProperty("area")
    private String area;

    @JsonProperty("isActive")
    private boolean isActive;

    public Machine() {}

    @JsonCreator
    public Machine(
            @JsonProperty("name") String name,
            @JsonProperty("heads") String heads,
            @JsonProperty("area") String area,
            @JsonProperty("isActive") boolean isActive) {
        this.name = name;
        this.heads = heads;
        this.area = area;
        this.isActive = true;
    }

    public Machine(Machine machine) {
        this.name = Objects.nonNull(machine.name) ? machine.name : this.name;
        this.heads = Objects.nonNull(machine.heads) ? machine.heads : this.heads;
        this.area = Objects.nonNull(machine.area) ? machine.area : this.area;
        this.isActive = machine.isActive;
        this.id = machine.id;
    }
}
