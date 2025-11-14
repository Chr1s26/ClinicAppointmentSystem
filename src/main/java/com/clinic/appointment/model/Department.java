package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
public class Department extends MasterData {

    private String departmentName;
    private String departmentDescription;

    @ManyToMany(mappedBy = "departments")
    private Set<Doctor> doctors = new HashSet<>();
}
