package com.enterprise.cleanqueen.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CloudflareR2Service {
    
    /**
     * Upload multiple images to Cloudflare R2 storage
     * @param images List of images to upload (max 2)
     * @return List of public URLs for uploaded images
     * @throws RuntimeException if upload fails
     */
    List<String> uploadImages(List<MultipartFile> images);
    
    /**
     * Delete images from Cloudflare R2 storage
     * @param imageUrls List of image URLs to delete
     * @return true if all images deleted successfully
     */
    boolean deleteImages(List<String> imageUrls);
}