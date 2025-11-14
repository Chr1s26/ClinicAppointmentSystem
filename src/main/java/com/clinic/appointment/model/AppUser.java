package com.clinic.appointment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_users")
public class AppUser extends MasterData {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    private LocalDate confirmedAt;

    // ---------- ROLES ----------
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // ---------- RELATIONSHIPS WITH DOMAIN USERS ----------
    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private Admin admin;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private Doctor doctor;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private Patient patient;


    // ---------- BUSINESS LOGIC ----------
    public boolean isAccountConfirmed() {
        return confirmedAt != null;
    }

    @Override
    public String toString() {
        return username;
    }
}
