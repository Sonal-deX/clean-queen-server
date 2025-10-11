package com.enterprise.cleanqueen.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Utility class for image compression and processing
 * Compresses images to target file size while maintaining quality
 */
@Component
public class ImageCompressionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ImageCompressionUtil.class);

    @Value("${image.compression.enabled:true}")
    private boolean compressionEnabled;

    @Value("${image.compression.target-size-kb:750}")
    private long targetSizeKb;

    @Value("${image.compression.quality:0.8}")
    private double initialQuality;

    @Value("${image.compression.max-dimension:1920}")
    private int maxDimension;

    /**
     * Compresses an image to target size while maintaining aspect ratio
     * 
     * @param originalFile Original MultipartFile
     * @return CompressedImageResult containing compressed image data and metadata
     * @throws IOException if compression fails
     */
    public CompressedImageResult compressImage(MultipartFile originalFile) throws IOException {
        if (!compressionEnabled) {
            return new CompressedImageResult(
                originalFile.getBytes(),
                originalFile.getContentType(),
                originalFile.getSize(),
                false
            );
        }

        long originalSizeKb = originalFile.getSize() / 1024;
        logger.info("Starting compression for image: {} ({}KB)", originalFile.getOriginalFilename(), originalSizeKb);

        // If already under target size, check if we need to resize for dimensions
        if (originalSizeKb <= targetSizeKb) {
            byte[] resizedImage = resizeIfNeeded(originalFile);
            long resizedSizeKb = resizedImage.length / 1024;
            
            if (resizedSizeKb <= targetSizeKb) {
                logger.info("Image already optimal: {}KB", resizedSizeKb);
                return new CompressedImageResult(
                    resizedImage,
                    getOutputContentType(originalFile.getContentType()),
                    resizedImage.length,
                    resizedImage.length < originalFile.getSize()
                );
            }
        }

        // Perform iterative compression
        byte[] compressedImage = performIterativeCompression(originalFile);
        long finalSizeKb = compressedImage.length / 1024;

        logger.info("Compression completed: {}KB -> {}KB ({}% reduction)", 
            originalSizeKb, finalSizeKb, 
            Math.round(((double)(originalSizeKb - finalSizeKb) / originalSizeKb) * 100));

        return new CompressedImageResult(
            compressedImage,
            getOutputContentType(originalFile.getContentType()),
            compressedImage.length,
            true
        );
    }

    /**
     * Resize image if it exceeds maximum dimensions
     */
    private byte[] resizeIfNeeded(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            
            if (originalImage == null) {
                throw new IOException("Unable to read image file");
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // Check if resizing is needed
            if (width <= maxDimension && height <= maxDimension) {
                return file.getBytes(); // No resizing needed
            }

            // Calculate new dimensions maintaining aspect ratio
            double aspectRatio = (double) width / height;
            int newWidth, newHeight;

            if (width > height) {
                newWidth = maxDimension;
                newHeight = (int) (maxDimension / aspectRatio);
            } else {
                newHeight = maxDimension;
                newWidth = (int) (maxDimension * aspectRatio);
            }

            logger.info("Resizing image from {}x{} to {}x{}", width, height, newWidth, newHeight);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(originalImage)
                    .size(newWidth, newHeight)
                    .outputFormat(getOutputFormat(file.getContentType()))
                    .outputQuality(initialQuality)
                    .toOutputStream(outputStream);

            return outputStream.toByteArray();
        }
    }

    /**
     * Perform iterative compression to reach target file size
     */
    private byte[] performIterativeCompression(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            BufferedImage originalImage = ImageIO.read(inputStream);
            
            if (originalImage == null) {
                throw new IOException("Unable to read image file");
            }

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            
            // Start with dimension reduction if image is too large
            if (width > maxDimension || height > maxDimension) {
                double aspectRatio = (double) width / height;
                if (width > height) {
                    width = maxDimension;
                    height = (int) (maxDimension / aspectRatio);
                } else {
                    height = maxDimension;
                    width = (int) (maxDimension * aspectRatio);
                }
            }

            double quality = initialQuality;
            double scaleFactor = 1.0;
            byte[] compressedImage = null;
            long targetSizeBytes = targetSizeKb * 1024;

            // Iterative compression with quality and size reduction
            for (int attempt = 0; attempt < 10; attempt++) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                
                int currentWidth = (int) (width * scaleFactor);
                int currentHeight = (int) (height * scaleFactor);

                Thumbnails.of(originalImage)
                        .size(currentWidth, currentHeight)
                        .outputFormat(getOutputFormat(file.getContentType()))
                        .outputQuality(quality)
                        .toOutputStream(outputStream);

                compressedImage = outputStream.toByteArray();
                long currentSize = compressedImage.length;

                logger.debug("Compression attempt {}: {}x{}, quality={}, size={}KB", 
                    attempt + 1, currentWidth, currentHeight, quality, currentSize / 1024);

                if (currentSize <= targetSizeBytes) {
                    logger.info("Target size reached in {} attempts", attempt + 1);
                    break;
                }

                // Adjust parameters for next iteration
                if (attempt < 5) {
                    // First, reduce quality
                    quality = Math.max(0.3, quality - 0.1);
                } else {
                    // Then, reduce dimensions
                    scaleFactor = Math.max(0.5, scaleFactor - 0.1);
                    quality = Math.max(0.3, quality - 0.05);
                }
            }

            return compressedImage;
        }
    }

    /**
     * Get output format based on input content type
     */
    private String getOutputFormat(String contentType) {
        if (contentType != null && contentType.toLowerCase().contains("png")) {
            return "png";
        }
        return "jpg"; // Default to JPEG for better compression
    }

    /**
     * Get output content type based on input content type
     */
    private String getOutputContentType(String inputContentType) {
        if (inputContentType != null && inputContentType.toLowerCase().contains("png")) {
            return "image/png";
        }
        return "image/jpeg"; // Default to JPEG
    }

    /**
     * Result class for compressed image data
     */
    public static class CompressedImageResult {
        private final byte[] imageData;
        private final String contentType;
        private final long sizeBytes;
        private final boolean wasCompressed;

        public CompressedImageResult(byte[] imageData, String contentType, long sizeBytes, boolean wasCompressed) {
            this.imageData = imageData;
            this.contentType = contentType;
            this.sizeBytes = sizeBytes;
            this.wasCompressed = wasCompressed;
        }

        public byte[] getImageData() {
            return imageData;
        }

        public String getContentType() {
            return contentType;
        }

        public long getSizeBytes() {
            return sizeBytes;
        }

        public long getSizeKb() {
            return sizeBytes / 1024;
        }

        public boolean wasCompressed() {
            return wasCompressed;
        }
    }
}