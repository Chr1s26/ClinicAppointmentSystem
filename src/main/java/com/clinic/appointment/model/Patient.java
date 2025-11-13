package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.GenderType;
import com.clinic.appointment.model.constant.PatientType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient extends UserMasterData {

    @Column
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private GenderType genderType;

//    @Enumerated(EnumType.ORDINAL)
    private PatientType patientType;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

}
