package com.example.demo.service.authentication;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.authentication.AuthenticationResponse;
import com.example.demo.dto.authentication.RegisterDTO;
import com.example.demo.dto.user.UserDTO;
import org.springframework.security.access.AccessDeniedException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(String username, String password);

    ResponseDTO registerNewUser(RegisterDTO registerDTO);
}
