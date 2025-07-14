package com.clinic.appointment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_users")
public class AppUser extends MasterData {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private LocalDate confirmedAt;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns=@JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Admin admin;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Doctor doctor;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    private Patient patient;

    public boolean isAccountConfirmed(){
        return this.confirmedAt != null;
    }

    @Override
    public String toString() {
        return username;
    }
}
