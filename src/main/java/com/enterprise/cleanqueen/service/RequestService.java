package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.request.CreateRequestRequest;
import com.enterprise.cleanqueen.dto.request.CreateRequestResponse;

public interface RequestService {

    CreateRequestResponse createRequest(CreateRequestRequest request, String userEmail);
}
