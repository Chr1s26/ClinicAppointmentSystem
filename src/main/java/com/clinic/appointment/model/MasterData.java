package com.clinic.appointment.model;


import com.clinic.appointment.model.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;

@Data
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class MasterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDate updatedAt;
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id",referencedColumnName = "ID")
    private AppUser createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id",referencedColumnName = "ID")
    private AppUser updatedBy;

    public boolean isDeleted(){
        if(this.status != null && this.status.equalsIgnoreCase(Status.DELETE.name())){
            return true;
        }
        return false;
    }

    public boolean isOwnRecord(Long id){
        if(id != null && this.getCreatedBy().getId() != null && this.getCreatedBy().getId().equals(id) && this.getCreatedBy() != null){
            return true;
        }
        return false;
    }

    public boolean isAdmin(){
        return this instanceof Admin;
    }

    public boolean isPatient(){
        return this instanceof Patient;
    }

    public boolean isDoctor(){
        return this instanceof Doctor;
    }

    public boolean isAppUser(){
        return this instanceof AppUser;
    }
}
