package com.clinic.appointment.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserMasterData extends MasterData {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String phone;
}
