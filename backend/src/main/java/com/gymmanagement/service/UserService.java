package com.gymmanagement.service;

import com.gymmanagement.dto.response.AuthResponse;
import com.gymmanagement.entity.User;
import com.gymmanagement.enums.Role;
import com.gymmanagement.exception.BadRequestException;
import com.gymmanagement.exception.ResourceNotFoundException;
import com.gymmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<AuthResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> AuthResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .role(user.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthResponse createUser(String username, String email, String password, String fullName, Role role) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken: " + username);
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already registered: " + email);
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(role != null ? role : Role.STAFF)
                .active(true)
                .build();

        User saved = userRepository.save(user);

        return AuthResponse.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .role(saved.getRole())
                .build();
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }
}
