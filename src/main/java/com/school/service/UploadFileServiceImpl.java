package com.school.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        File fileObj = convertMutiPartFileToFile(file);
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename().replace(" ", "");
        s3Client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
        fileObj.delete();
        return filename;
    }

    @Override
    public boolean deleteFile(String filename) {
        if (filename != null && filename.length() > 0) {
            s3Client.deleteObject(bucketName, filename);
            return true;
        }

        return false;
    }

    @Override
    public byte[] cargarImagen(String nombreImagen)  {
        S3Object s3Object = s3Client.getObject(bucketName, nombreImagen);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File convertMutiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }

        return convertedFile;
    }
}
