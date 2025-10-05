package com.enterprise.cleanqueen.service.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.enterprise.cleanqueen.service.CloudflareR2Service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class CloudflareR2ServiceImpl implements CloudflareR2Service {

    private static final Logger logger = LoggerFactory.getLogger(CloudflareR2ServiceImpl.class);

    @Value("${cloudflare.r2.account-id}")
    private String accountId;

    @Value("${cloudflare.r2.access-key}")
    private String accessKey;

    @Value("${cloudflare.r2.secret-key}")
    private String secretKey;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.public-domain:}")
    private String publicDomain;

    private S3Client s3Client;

    private S3Client getS3Client() {
        if (s3Client == null) {
            // Cloudflare R2 endpoint format
            String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);
            
            AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
            
            s3Client = S3Client.builder()
                    .endpointOverride(URI.create(endpoint))
                    .credentialsProvider(StaticCredentialsProvider.create(credentials))
                    .region(Region.US_EAST_1) // R2 uses auto region, but SDK requires one
                    .build();
        }
        return s3Client;
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }

        if (images.size() > 2) {
            throw new RuntimeException("Maximum 2 images are allowed");
        }

        List<String> uploadedUrls = new ArrayList<>();
        List<String> uploadedKeys = new ArrayList<>();

        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                try {
                    String imageKey = uploadSingleImage(image);
                    String imageUrl = generatePublicUrl(imageKey);
                    uploadedUrls.add(imageUrl);
                    uploadedKeys.add(imageKey);
                    logger.info("Successfully uploaded image: {}", imageUrl);
                } catch (Exception e) {
                    // If any image fails, delete already uploaded images and throw exception
                    logger.error("Failed to upload image: {}", image.getOriginalFilename(), e);
                    if (!uploadedKeys.isEmpty()) {
                        deleteImagesByKeys(uploadedKeys);
                    }
                    throw new RuntimeException("Failed to upload image: " + image.getOriginalFilename() + ". " + e.getMessage());
                }
            }
        }

        return uploadedUrls;
    }

    private String uploadSingleImage(MultipartFile image) throws IOException {
        validateImage(image);

        String fileName = generateUniqueFileName(image.getOriginalFilename());
        String key = "reviews/" + fileName; // Store in reviews folder

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(image.getContentType())
                    .contentLength(image.getSize())
                    .build();

            RequestBody requestBody = RequestBody.fromBytes(image.getBytes());
            
            getS3Client().putObject(putRequest, requestBody);
            
            return key;
        } catch (S3Exception e) {
            logger.error("S3 error uploading image to R2", e);
            throw new RuntimeException("Failed to upload image to R2: " + e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            logger.error("Error uploading image to R2", e);
            throw new RuntimeException("Failed to upload image to R2: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return true;
        }

        List<String> keys = new ArrayList<>();
        for (String url : imageUrls) {
            String key = extractKeyFromUrl(url);
            if (key != null) {
                keys.add(key);
            }
        }

        return deleteImagesByKeys(keys);
    }

    private boolean deleteImagesByKeys(List<String> keys) {
        boolean allDeleted = true;
        
        for (String key : keys) {
            try {
                DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();

                getS3Client().deleteObject(deleteRequest);
                logger.info("Successfully deleted image with key: {}", key);
            } catch (S3Exception e) {
                logger.error("Failed to delete image with key: {}", key, e);
                allDeleted = false;
            }
        }

        return allDeleted;
    }

    private void validateImage(MultipartFile image) {
        // Check file size (max 10MB)
        long maxSizeInBytes = 10 * 1024 * 1024; // 10MB
        if (image.getSize() > maxSizeInBytes) {
            throw new RuntimeException("Image size exceeds maximum limit of 10MB");
        }

        // Check file type
        String contentType = image.getContentType();
        if (contentType == null || (!contentType.startsWith("image/jpeg") && 
                                   !contentType.startsWith("image/jpg") && 
                                   !contentType.startsWith("image/png") && 
                                   !contentType.startsWith("image/gif") && 
                                   !contentType.startsWith("image/webp"))) {
            throw new RuntimeException("Invalid image format. Supported formats: JPEG, PNG, GIF, WebP");
        }
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.lastIndexOf('.') > 0) {
            extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        return "review_" + UUID.randomUUID().toString() + extension;
    }

    private String generatePublicUrl(String key) {
        if (publicDomain != null && !publicDomain.isEmpty()) {
            // Use custom public domain if configured
            return String.format("https://%s/%s", publicDomain, key);
        } else {
            // Use default R2 public URL format
            return String.format("https://%s.r2.dev/%s", bucketName, key);
        }
    }

    private String extractKeyFromUrl(String url) {
        try {
            if (publicDomain != null && !publicDomain.isEmpty() && url.contains(publicDomain)) {
                // Extract key from custom domain URL
                String[] parts = url.split(publicDomain + "/", 2);
                return parts.length > 1 ? parts[1] : null;
            } else if (url.contains(".r2.dev/")) {
                // Extract key from default R2 URL
                String[] parts = url.split(".r2.dev/", 2);
                return parts.length > 1 ? parts[1] : null;
            }
        } catch (Exception e) {
            logger.warn("Failed to extract key from URL: {}", url);
        }
        return null;
    }
}