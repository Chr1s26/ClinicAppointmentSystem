package com.clinic.appointment.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.clinic.appointment.exception.ResourceNotFoundException;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.ExportListing;
import com.clinic.appointment.model.FileStorage;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.ExportListingRepository;
import com.clinic.appointment.repository.FileStorageRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    public static final String UPLOAD_DIRECTORY = "/Users/chr1skak/Documents/clinic_file_import";
    private final AuthService authService;
    private final ExportListingRepository exportListingRepository;

    @Value("${cloud.aws.bucket}")
    private String s3BucketName;

    private String s3BaseUrl="https://%s.s3.%s.amazonaws.com/%s";

    @Value("${cloud.aws.region.static}")
    private String region;

    private final FileStorageRepository fileStorageRepository;
    private final AmazonS3 amazonS3;

    public String getFileName(FileType fileType, Long fileId) {
        return fileStorageRepository
                .findTopByFileTypeAndFileIdOrderByCreatedAtDesc(fileType, fileId)
                .map(fs -> getFileUrl(fs.getKey(), fs.getServiceName()))
                .orElse("/images/default-profile.png");
    }

    public String getFileUrl(String fileKey,String serviceName){
        if("local".equalsIgnoreCase(serviceName)){
            return "/files/"+fileKey;
        }else{
            String url = String.format(s3BaseUrl,s3BucketName,region,fileKey);
            return String.format(s3BaseUrl,s3BucketName,region,fileKey);
        }
    }

    public void handleFileUpload(MultipartFile file, FileType fileType, Long id, String serviceName) {
        if(file.isEmpty()){
            throw new RuntimeException("Empty file");
        }

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedfileName = uuid + fileExtension;

        try{
            if("local".equalsIgnoreCase(serviceName)){
                File dir = new File(UPLOAD_DIRECTORY);
                if(!dir.exists()) dir.mkdirs();

                String filePath = UPLOAD_DIRECTORY + File.separator + storedfileName;
                file.transferTo(new File(filePath));
                System.out.println("File upload successfully to Local");
            }else{
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentType(file.getContentType());
                objectMetadata.setContentLength(file.getSize());
                amazonS3.putObject(s3BucketName, storedfileName, file.getInputStream(), objectMetadata);
                log.info("File upload Successful to S3 : {}",storedfileName);
            }

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(file.getOriginalFilename());
            fileStorage.setKey(storedfileName);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setServiceName(serviceName);
            fileStorage.setFileType(fileType);
            fileStorage.setFileId(id);
            fileStorage.setContentType(file.getContentType());

            fileStorageRepository.save(fileStorage);
        }catch (IOException ex){
            throw new RuntimeException("Failed to upload file : "+ex.getMessage());
        }
    }

    public FileStorage saveExportFileWithStatus(ByteArrayInputStream inputStream, ExportListing exportListing) throws IOException {

        byte[] bytes = inputStream.readAllBytes();
        ByteArrayInputStream uploadStream = new ByteArrayInputStream(bytes);
        long fileSize = bytes.length;

        AppUser user = authService.getCurrentUser();
        String fileName = exportListing.getFileName();

        String uuid = UUID.randomUUID().toString();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String s3Key = uuid + extension;

        FileStorage fileStorage = new FileStorage();

        fileStorage.setFileName(fileName);
        fileStorage.setFileSize(fileSize);
        fileStorage.setKey(s3Key);
        fileStorage.setServiceName("s3");
        fileStorage.setFileType(exportListing.getFileType());
        fileStorage.setFileId(exportListing.getId());
        fileStorage.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        fileStorage.setStatus(StatusType.COMPLETED);
        fileStorage.setCreatedAt(LocalDateTime.now());
        fileStorage.setCreatedBy(user);

        fileStorage = fileStorageRepository.save(fileStorage);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        amazonS3.putObject("hotel-export-report-bucket", s3Key, uploadStream, metadata);
        log.info("Export file uploaded successfully to S3: {}", s3Key);

        return fileStorage;
    }

    public void downloadExportFile(ExportListing exportListing, HttpServletResponse response, boolean asZip) throws IOException {

        FileStorage file = fileStorageRepository.findTopByFileIdAndFileTypeOrderByCreatedAtDesc(exportListing.getId(), exportListing.getFileType());
        if (file == null) {
            throw new ResourceNotFoundException("FileStorage", file, "fileId", "/exports","File not found");
        }
        S3Object s3Object = amazonS3.getObject("hotel-export-report-bucket", file.getKey());
        S3ObjectInputStream s3is = s3Object.getObjectContent();

        if (asZip) {
            // Deliver as ZIP
            response.setContentType("application/zip");
            String zipFileName = exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_").replace(".xlsx", ".zip");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");

            // Read Excel bytes fully
            byte[] excelBytes = s3is.readAllBytes();

            // Create ZIP in memory
            try (ServletOutputStream sos = response.getOutputStream();
                 ZipOutputStream zos = new ZipOutputStream(sos)) {

                ZipEntry entry = new ZipEntry(exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
                zos.putNextEntry(entry);
                zos.write(excelBytes);
                zos.closeEntry();
                zos.finish();
            }
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String excelFileName = exportListing.getFileName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + excelFileName + "\"");

            try (ServletOutputStream sos = response.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int len;
                while ((len = s3is.read(buffer)) != -1) {
                    sos.write(buffer, 0, len);
                }
            }
        }
    }


    public List<String> getFileNames(FileType fileType, Long fileId) {
        List<FileStorage> files = fileStorageRepository.findAllByFileTypeAndFileIdOrderByCreatedAtDesc(fileType,fileId);
        if(files.isEmpty()){
            return List.of("/images/default-profile.png");
        }
        return files.stream().map(fs -> getFileUrl(fs.getKey(), fs.getServiceName())).toList();
    }

    public void saveExportFileWithFailStatus(ExportListing exportListing) {
        exportListing.setStatus(StatusType.FAIL);
        exportListingRepository.save(exportListing);
    }

    public String getBucketName(){
        return s3BucketName;
    }
}
