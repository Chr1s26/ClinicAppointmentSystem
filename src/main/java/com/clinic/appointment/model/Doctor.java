package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.GenderType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor extends UserMasterData {

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column
    private String address;

    @Column
    private GenderType genderType;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "doctor_department",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "dept_id"))
    private Set<Department> departments = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "app_user_id")
    @JsonIgnore
    private AppUser appUser;

    public void addDepartment(Department department) {
        this.departments.add(department);
        department.getDoctors().add(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Doctor{" +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", genderType=" + genderType +
                '}';
    }
}
