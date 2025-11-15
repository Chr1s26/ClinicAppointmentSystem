package com.clinic.appointment.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public abstract class UserMasterData extends MasterData {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String phone;
}
