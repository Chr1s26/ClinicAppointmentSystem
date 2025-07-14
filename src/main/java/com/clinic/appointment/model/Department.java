package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "departments")
public class Department extends MasterData {
    @Column
    private String departmentName;

    @Column
    private String departmentDescription;

    @ManyToMany(mappedBy = "departments")
    private Set<Doctor> doctors = new HashSet<>();

    @Override
    public String toString() {
        return departmentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department other = (Department) o;
        return getId() != null && getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}