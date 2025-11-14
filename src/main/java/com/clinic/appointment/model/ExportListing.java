package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.FileType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExportListing extends MasterData{
    @Column
    private String fileName;
    @Column
    private FileType fileType;
}
