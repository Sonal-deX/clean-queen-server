package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.user.UpdateProfileRequest;
import com.enterprise.cleanqueen.dto.user.UpdateProfileResponse;
import com.enterprise.cleanqueen.dto.user.UserProfileResponse;

public interface UserService {
    
    UserProfileResponse getUserProfile(String email);
    
    UpdateProfileResponse updateUserProfile(String email, UpdateProfileRequest request);
}