package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.GenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin extends UserMasterData {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    private GenderType genderType;
}
