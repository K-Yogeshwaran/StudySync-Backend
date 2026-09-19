package space.yogeshwarank.studysync.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import space.yogeshwarank.studysync.dto.LoginRequest;
import space.yogeshwarank.studysync.dto.LoginResponse;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.EmailAlreadyExistsException;
import space.yogeshwarank.studysync.exception.InvalidCredentialException;
import space.yogeshwarank.studysync.repository.UserRepository;
import space.yogeshwarank.studysync.security.JwtService;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Test
    public void register_shouldThrowException_whenEmailAlreadyExists(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("admin");
        registerRequest.setEmail("admin@gmail.com");
        registerRequest.setPassword("admin1234");

        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(registerRequest));
    }

    @Test
    public void register_userShouldBeCreated_whenEmailDoesNotExist(){

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail("testuser@gmail.com");
        registerRequest.setPassword("testuser1234");

        User newUser = new User();
        newUser.setName("Test User");
        newUser.setEmail("testuser@gmail.com");
        newUser.setPassword("hashed-test-password");

        when(userRepository.existsByEmail("testuser@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("testuser1234")).thenReturn("hashed-test-password");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User user = authService.register(registerRequest);

        assertEquals("Test User", user.getName());
        assertEquals("testuser@gmail.com", user.getEmail());
        assertEquals("hashed-test-password", user.getPassword());

    }

    @Test
    public void login_shouldThrowException_whenTheUserIsNotFound(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@gmail.com");
        loginRequest.setPassword("hashed-password");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialException.class, () -> authService.login(loginRequest));
    }

    @Test
    public void login_shouldThrowException_whenThePasswordIsIncorrect(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@gmail.com");
        loginRequest.setPassword("Incorrect password");

        User user = new User();
        UUID uuid = UUID.randomUUID();
        user.setId(uuid);
        user.setName("Admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("Correct password");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialException.class, () -> authService.login(loginRequest));
    }


    @Test
    public void login_shouldReturnAccessToken_whenCredentialsAreCorrect(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@gmail.com");
        loginRequest.setPassword("admin1234");

        User user = new User();
        user.setName("Admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("hashed admin password");

        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("secret jwt token");

        LoginResponse loginResponse = authService.login(loginRequest);
        assertEquals("secret jwt token", loginResponse.getAccessToken());
        verify(passwordEncoder).matches("admin1234", "hashed admin password");

    }
}
