package com.example.demo.service.user;

import com.example.demo.controller.exception.NotFoundException;
import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.user.User;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User loadUserByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username));
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        return user.get();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
