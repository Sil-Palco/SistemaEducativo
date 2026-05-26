package com.school.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface UploadFileService {

    public String uploadFile(MultipartFile file) throws IOException;

    public boolean deleteFile(String filename);

    public byte[] cargarImagen(String nombreImagen) throws MalformedURLException;

}
