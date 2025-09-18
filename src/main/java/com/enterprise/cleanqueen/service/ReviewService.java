package com.enterprise.cleanqueen.service;

import com.enterprise.cleanqueen.dto.review.CreateReviewRequest;
import com.enterprise.cleanqueen.dto.review.CreateReviewResponse;

public interface ReviewService {

    CreateReviewResponse createReview(CreateReviewRequest request, String supervisorEmail);
}
