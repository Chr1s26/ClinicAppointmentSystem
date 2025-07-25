package com.clinic.appointment.model;

import com.clinic.appointment.model.constant.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Table
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FileStorage extends MasterData {
    private FileType fileType;
    private Long fileId;
    private String fileName;
    private String key;
    private String contentType;
    private long fileSize;
    private String serviceName;
}
