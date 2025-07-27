package com.enterprise.cleanqueen.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enterprise.cleanqueen.dto.request.CreateRequestRequest;
import com.enterprise.cleanqueen.dto.request.CreateRequestResponse;
import com.enterprise.cleanqueen.entity.CleaningRequest;
import com.enterprise.cleanqueen.entity.User;
import com.enterprise.cleanqueen.enums.CleaningRequestStatus;
import com.enterprise.cleanqueen.exception.BusinessException;
import com.enterprise.cleanqueen.repository.CleaningRequestRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.EmailService;
import com.enterprise.cleanqueen.service.RequestService;
import com.enterprise.cleanqueen.util.CodeGenerator;
import com.enterprise.cleanqueen.util.ValidationUtil;


@Service
@Transactional
public class RequestServiceImpl implements RequestService {

    private static final Logger logger = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private CleaningRequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ValidationUtil validationUtil;

    @Override
    public CreateRequestResponse createRequest(CreateRequestRequest request, String userEmail) {
        // Validate email format
        if (!validationUtil.isValidEmail(userEmail)) {
            throw new BusinessException("Invalid user email format");
        }

        // Validate phone number if provided
        if (request.getPhoneNumber() != null && !validationUtil.isValidPhoneNumber(request.getPhoneNumber())) {
            throw new BusinessException("Invalid phone number format");
        }

        // Sanitize inputs for security
        String cleanUserEmail = validationUtil.sanitizeInput(userEmail).toLowerCase();
        String cleanName = validationUtil.sanitizeInput(request.getName());
        String cleanPhoneNumber = request.getPhoneNumber() != null ? 
            validationUtil.sanitizeInput(request.getPhoneNumber()) : null;
        String cleanDescription = validationUtil.sanitizeInput(request.getDescription());

        // Find user
        User user = userRepository.findByEmail(cleanUserEmail)
                .orElseThrow(() -> new BusinessException("User not found"));

        // Create cleaning request
        CleaningRequest cleaningRequest = new CleaningRequest();
        cleaningRequest.setId(codeGenerator.generateRequestId());
        cleaningRequest.setName(cleanName);
        cleaningRequest.setEmail(user.getEmail()); // Use authenticated user's email
        cleaningRequest.setPhoneNumber(cleanPhoneNumber);
        cleaningRequest.setDescription(cleanDescription);
        cleaningRequest.setStatus(CleaningRequestStatus.PENDING);
        cleaningRequest.setUserId(user.getId());

        // Save request
        requestRepository.save(cleaningRequest);

        // Send confirmation email to customer
        emailService.sendRequestConfirmationEmail(
                user.getEmail(),
                cleaningRequest.getId(),
                cleanName
        );

        logger.info("Cleaning request created successfully: {}", cleaningRequest.getId());

        return new CreateRequestResponse(
                true,
                "Cleaning request submitted successfully. We will contact you soon.",
                cleaningRequest.getId(),
                cleaningRequest.getStatus().name()
        );
    }

}
