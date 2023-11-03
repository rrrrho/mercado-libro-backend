package com.mercadolibro.service;

import com.mercadolibro.dto.S3ObjectDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {
    /**
     * Uploads a single MultipartFile to the S3 repository.
     *
     * @param multipartFile The MultipartFile to be uploaded.
     * @return An S3ObjectDTO representing the uploaded file's metadata.
     */
    S3ObjectDTO uploadFile(MultipartFile multipartFile);

    /**
     * Uploads a list of MultipartFiles to the S3 repository.
     *
     * @param multipartFiles The list of MultipartFiles to be uploaded.
     * @return A list of S3ObjectDTOs representing metadata of the uploaded files.
     */
    List<S3ObjectDTO> uploadFiles(List<MultipartFile> multipartFiles);

    /**
     * Deletes a file from the S3 repository by its file name.
     *
     * @param fileName The name of the file to be deleted from the S3 repository.
     */
    void deleteFile(String fileName);
}
