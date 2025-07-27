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
import com.enterprise.cleanqueen.repository.CleaningRequestRepository;
import com.enterprise.cleanqueen.repository.UserRepository;
import com.enterprise.cleanqueen.service.EmailService;
import com.enterprise.cleanqueen.service.RequestService;
import com.enterprise.cleanqueen.util.CodeGenerator;


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

    @Override
    public CreateRequestResponse createRequest(CreateRequestRequest request, String userEmail) {
        // Find user
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create cleaning request
        CleaningRequest cleaningRequest = new CleaningRequest();
        cleaningRequest.setId(codeGenerator.generateRequestId());
        cleaningRequest.setName(request.getName());
        cleaningRequest.setEmail(user.getEmail()); // Use authenticated user's email
        cleaningRequest.setPhoneNumber(request.getPhoneNumber());
        cleaningRequest.setDescription(request.getDescription());
        cleaningRequest.setStatus(CleaningRequestStatus.PENDING);
        cleaningRequest.setUserId(user.getId());

        // Save request
        requestRepository.save(cleaningRequest);

        // Send confirmation email to customer
        emailService.sendRequestConfirmationEmail(
                user.getEmail(),
                cleaningRequest.getId(),
                request.getName()
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
