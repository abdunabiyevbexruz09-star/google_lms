package org.example.google_lms.service;

import lombok.RequiredArgsConstructor;
import org.example.google_lms.domain.dto.request.RegisterRequest;
import org.example.google_lms.domain.entity.user.Role;
import org.example.google_lms.domain.entity.user.Status;
import org.example.google_lms.domain.entity.user.User;
import org.example.google_lms.exception.BadRequestException;
import org.example.google_lms.exception.ConflictException;
import org.example.google_lms.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final EmailSenderService emailSenderService;
    private final Random random;

    public void registerStudent(RegisterRequest request) {

        userRepository.findByEmail(request.email())
                .ifPresent(user -> {
                    if (user.getStatus().equals(Status.UNVERIFIED)) {
                        userRepository.delete(user);
                    } else {
                        throw new ConflictException("User already exists");
                    }
                });


        User user = User.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .password(encoder.encode(request.password()))
                .role(Role.STUDENT)
                .status(Status.UNVERIFIED)
                .balance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .build();

        emailSenderService.sendOtpCode(request.email(), random.nextLong(1000, 9999));

        userRepository.save(user);
    }

/*    public void verifyCode(VerifyCodeRequest request) {

        Long cachedCode = redisTemplate.opsForValue().get(request.phoneNumber());

        if (cachedCode == null || !cachedCode.equals(request.code())) {
            throw new BadRequestException("Invalid or expired verification code");
        }

        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        redisTemplate.delete(request.phoneNumber());
    }

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.phoneNumber())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!user.getStatus().equals(Status.ACTIVE)) {
            throw new UnauthorizedException("Account not verified. Please verify your account.");
        }

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.phoneNumber(),
                        request.password()
                )
        );

        String jwtToken = jwtService.generateToken(user);
        userRepository.save(user);
        return new TokenResponse(jwtToken);
    }*/

}