package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.GenderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin extends UserMasterData {

    @OneToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;
}
