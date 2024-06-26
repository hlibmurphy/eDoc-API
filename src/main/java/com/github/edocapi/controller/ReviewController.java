package com.github.edocapi.controller;

import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.model.User;
import com.github.edocapi.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    public List<ReviewDto> getAllReviews(Pageable pageable) {
        return reviewService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(@Valid @RequestBody CreateReviewRequestDto reviewRequestDto,
                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return reviewService.save(reviewRequestDto, user);
    }
}
