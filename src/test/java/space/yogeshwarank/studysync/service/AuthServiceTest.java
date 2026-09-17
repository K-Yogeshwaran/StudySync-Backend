package space.yogeshwarank.studysync.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import space.yogeshwarank.studysync.dto.RegisterRequest;
import space.yogeshwarank.studysync.entity.User;
import space.yogeshwarank.studysync.exception.EmailAlreadyExistsException;
import space.yogeshwarank.studysync.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
}
