package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctors")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Doctor extends UserMasterData {

    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "doctor_department",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id")
    )
    private Set<Department> departments = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    @JsonIgnore
    private AppUser appUser;

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.getDoctors().add(this);
    }
}
