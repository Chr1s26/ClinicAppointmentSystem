package com.clinic.appointment.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ExportListingService {
    private final ExportListingRepository exportListingRepository;
    private final AuthService authService;
    private final FileStorageRepository fileStorageRepository;
    private final AmazonS3 amazonS3;

    public ExportListingService(ExportListingRepository exportListingRepository, AuthService authService, FileStorageRepository fileStorageRepository, AmazonS3 amazonS3) {
        this.exportListingRepository = exportListingRepository;
        this.authService = authService;
        this.fileStorageRepository = fileStorageRepository;
        this.amazonS3 = amazonS3;
    }

    public ExportListing saveExportListing(String entityName, FileType fileType) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        String fileName = entityName + "-List-" + timestamp + ".xlsx";

        ExportListing listing = new ExportListing();
        listing.setFileName(fileName);
        listing.setFileType(fileType);
        listing.setCreatedAt(LocalDateTime.now());
        listing.setCreatedBy(authService.getCurrentUser());
        listing.setStatus(StatusType.COMPLETED);
        return exportListingRepository.save(listing);
    }

    public List<ExportListing> getAllExports() {
        return exportListingRepository.findAll();
    }

    public void deleteExportListing(Long id) {
        Optional<ExportListing> exportListingOp = exportListingRepository.findById(id);

        if (!exportListingOp.isPresent()) {
            throw new ResourceNotFoundException("export",exportListingOp.get(),"id","/exports","Export listing not found");
        }
        ExportListing exportListing = exportListingOp.get();

        FileStorage file = fileStorageRepository.findTopByFileIdAndFileTypeOrderByCreatedAtDesc(
                exportListing.getId(), exportListing.getFileType()
        );

        if (file != null) {
            try {
                amazonS3.deleteObject(new DeleteObjectRequest("hotel-export-report-bucket", file.getKey()));
                log.info("Deleted file from S3: {}", file.getKey());
                fileStorageRepository.delete(file);
            } catch (Exception e) {
                log.error("Failed to delete file from S3: {}", e.getMessage(), e);
                throw new RuntimeException("Failed to delete file from S3: " + e.getMessage());
            }
        } else {
            log.warn("No file found for export listing ID {}", id);
        }
        exportListingRepository.delete(exportListing);
        log.info("Deleted ExportListing with ID {}", id);
    }

}