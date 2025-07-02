package com.clinic.appointment.service;

import com.clinic.appointment.model.FileStorage;
import com.clinic.appointment.model.constant.FileType;
import com.clinic.appointment.repository.FileStorageRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Data
@Service
@AllArgsConstructor
public class FileService {

    public static final String UPLOAD_DIRECTORY = "/Users/chr1skak/Documents/clinic_file_import";
    private final FileStorageRepository fileStorageRepository;


    public String getFileName(FileType fileType, Long fileId) {
        List<FileStorage> fileStorageList = fileStorageRepository.findByFileTypeAndFileId(fileType,fileId);
        if (fileStorageList.isEmpty()) {
            return null;
        }
        return getFileUrl(fileStorageList.get(0).getKey());
    }

    public String getFileUrl(String fileKey){
        return "/files/"+fileKey;
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
            File dir = new File(UPLOAD_DIRECTORY);
            if(!dir.exists()) dir.mkdirs();

            String filePath = UPLOAD_DIRECTORY + File.separator + storedfileName;
            file.transferTo(new File(filePath));

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
}
