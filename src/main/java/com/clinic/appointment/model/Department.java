package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @OneToMany(mappedBy = "department")
    private List<Doctor> doctors;

    @Override
    public String toString() {
        return departmentName;
    }
}