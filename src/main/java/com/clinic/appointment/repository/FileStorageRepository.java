package com.clinic.appointment.repository;

import com.clinic.appointment.model.FileStorage;
import com.clinic.appointment.model.constant.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {

    List<FileStorage> findByFileTypeAndFileId(FileType fileType, Long fileId);

    FileStorage findTopByFileIdAndFileTypeOrderByCreatedAtDesc(Long id, FileType fileType);

    List<FileStorage> findAllByFileTypeAndFileIdOrderByCreatedAtDesc(FileType fileType, Long fileId);

    Optional<FileStorage> findTopByFileTypeAndFileIdOrderByCreatedAtDesc(FileType fileType, Long fileId);
}
