package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String departmentName;

    @Column
    private String departmentDescription;

    @Column
    private LocalDate createdAt;

    @Column
    private LocalDate updatedAt;

    @ManyToMany
    @JoinTable(name = "doctor_department",
        joinColumns = @JoinColumn(name = "dept_id"),
        inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    private Set<Doctor> doctors = new HashSet<>();

    @Override
    public String toString() {
        return departmentName;
    }
}