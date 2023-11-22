package com.example.demo.service.user;

import com.example.demo.enums.ErrorMessage;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User loadUserByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username));
        if (user.isEmpty()) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        return user.get();
    }

    public User save(User user) {
        Role roleEntity = roleRepository.findByName("USER").get();
        user.builder()
                .roles(Collections.singletonList(roleEntity))
                .build();
        return userRepository.save(user);
    }
}
