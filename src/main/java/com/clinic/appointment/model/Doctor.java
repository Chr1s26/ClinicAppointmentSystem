package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column
    private String phone;

    @Column
    private String address;

    @Column
    private GenderType genderType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "doctor_department",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private Set<Department> departments = new HashSet<>();

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.getDoctors().add(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
