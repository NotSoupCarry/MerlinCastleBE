package com.soup.merlinCastleBE.service;

import com.soup.merlinCastleBE.dto.*;
import com.soup.merlinCastleBE.enums.Role;
import com.soup.merlinCastleBE.model.User;
import com.soup.merlinCastleBE.repository.UserRepository;
import com.soup.merlinCastleBE.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.soup.merlinCastleBE.dto.LoginDTOs.RegisterRequest;
import com.soup.merlinCastleBE.dto.LoginDTOs.LoginRequest;
import com.soup.merlinCastleBE.dto.LoginDTOs.AuthResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email già registrata");
        }
        if (userRepository.existsByUsername(req.username())) {
            throw new IllegalArgumentException("Username già in uso");
        }

        User user = User.builder()
                .username(req.username())
                .email(req.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.password()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password()));

        User user = userRepository.findByEmail(req.email().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Credenziali non valide"));

        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse refresh(String refreshToken) {
        String email = jwtUtils.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Token non valido"));

        if (!jwtUtils.isTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Refresh token non valido o scaduto");
        }
        return toResponse(user);
    }

    private AuthResponse toResponse(User user) {
        return new AuthResponse(
                jwtUtils.generateAccessToken(user),
                jwtUtils.generateRefreshToken(user),
                user.getId(),
                user.getDisplayName(),
                user.getRole());
    }
}