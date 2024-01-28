package com.example.demo.service.user;

import com.example.demo.model.user.UserProfile;
import com.example.demo.repository.user.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfile loadUserProfileByUserId(UUID userId) {
        Optional<UserProfile> userProfile = Optional.ofNullable(userProfileRepository.findByUserId(userId));
        if (userProfile.isEmpty()) {
            throw new RuntimeException("User profile not found");
        }
        return userProfile.get();
    }
}
