package com.borrowbay.borrowbay.service;

import com.borrowbay.borrowbay.entity.User;
import com.borrowbay.borrowbay.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(User user) {

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        return userRepository.save(user);
    }

    public User findByEmail(String email) {

        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    public User updateUser(User user) {

        return userRepository.save(user);
    }

    public boolean checkPassword(
            String rawPassword,
            String encodedPassword) {

        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }

    public void changePassword(
            User user,
            String newPassword) {

        user.setPassword(
                passwordEncoder.encode(
                        newPassword
                )
        );

        userRepository.save(user);
    }
}