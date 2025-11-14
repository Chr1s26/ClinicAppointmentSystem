package com.clinic.appointment.model;


import com.clinic.appointment.model.constant.StatusType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class MasterData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatusType status;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id",referencedColumnName = "ID")
    @JsonIgnore
    private AppUser createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_id",referencedColumnName = "ID")
    @JsonIgnore
    private AppUser updatedBy;

    @JsonIgnore
    public boolean isDeleted(){
        if(this.status != null && this.status.equals(StatusType.DELETED)){
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isOwnRecord(Long id) {
        return this.createdBy != null &&
                this.createdBy.getId() != null &&
                id != null &&
                this.createdBy.getId().equals(id);
    }

}
