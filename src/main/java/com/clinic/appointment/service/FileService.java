package com.clinic.appointment.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.clinic.appointment.model.AppUser;
import com.clinic.appointment.model.ExportListing;
import com.clinic.appointment.model.FileStorage;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.model.constant.StatusType;
import com.clinic.appointment.repository.ExportListingRepository;
import com.clinic.appointment.repository.FileStorageRepository;
import lombok.AllArgsConstructor;
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
        List<FileStorage> fileStorageList = fileStorageRepository.findByFileTypeAndFileId(fileType,fileId);

        if (fileStorageList.isEmpty()) {
            return null;
        }
        FileStorage fileStorage = fileStorageList.get(0);
        return getFileUrl(fileStorageList.get(0).getKey(), fileStorage.getServiceName());
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

    public void saveExportFileWithFailStatus(ExportListing exportListing) {
        exportListing.setStatus(StatusType.FAIL);
        exportListingRepository.save(exportListing);
    }
}
